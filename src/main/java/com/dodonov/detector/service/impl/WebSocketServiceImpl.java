package com.dodonov.detector.service.impl;

import com.dodonov.detector.binance.api.client.BinanceApiWebSocketClient;
import com.dodonov.detector.binance.api.client.domain.event.AbnormalTradingNoticeEvent;
import com.dodonov.detector.binance.api.client.domain.event.CandlestickEvent;
import com.dodonov.detector.binance.api.client.domain.general.ExchangeInfo;
import com.dodonov.detector.binance.api.client.domain.general.SymbolInfo;
import com.dodonov.detector.binance.api.client.domain.general.SymbolStatus;
import com.dodonov.detector.binance.api.client.domain.market.CandlestickInterval;
import com.dodonov.detector.dto.AggTradeDto;
import com.dodonov.detector.dto.StatisticsDto;
import com.dodonov.detector.entity.AbnormalTrading;
import com.dodonov.detector.entity.AggTrade;
import com.dodonov.detector.entity.Kline;
import com.dodonov.detector.entity.Statistics;
import com.dodonov.detector.service.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.dodonov.detector.util.Constant.BASE_API;
import static com.dodonov.detector.util.Constant.BTC;
import static com.dodonov.detector.util.Constant.EXCHANGE_INFO;
import static com.dodonov.detector.util.Constant.USDT;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {
    private final DataProcessorService dataProcessorService;
    private final AbnormalTradingService abnormalTradingService;
    private final ModelMapper mapper;
    private final RestTemplate restTemplate;
    private final BinanceApiWebSocketClient client;
    private final KlineService klineService;
    private final List<Kline> klines = new ArrayList<>();
    private final AggTradeService aggTradeService;
    private final List<AggTrade> aggTrades = new ArrayList<>();
    private final StatisticsService statisticsService;
    private final List<Statistics> statistics = new ArrayList<>();
    private Closeable klineWebSocket;
    private Closeable aggTradeWebSocket;
    private Closeable statisticsWebSocket;
    private Closeable abnormalTradingWebSocket;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private Integer batchSize;
    private AtomicInteger klineCounter = new AtomicInteger();
    private AtomicInteger aggTradeCounter = new AtomicInteger();

    @Override
    @SneakyThrows
    public void initKlinesWS() {
        //ExchangeInfo info = getExchangeInfo();
        //checkWebSocket(klineWebSocket);
        //klineWebSocket = processKlineEvents(Objects.requireNonNull(info).getSymbols().stream()
        //        .filter(s -> (s.getSymbol().endsWith(BTC) || s.getSymbol().endsWith(USDT)) && SymbolStatus.TRADING.equals(s.getStatus()))
        //        .map(SymbolInfo::getSymbol)
        //        .collect(Collectors.toList()));
    }

    @Override
    @SneakyThrows
    public void initAggTradesWS() {
        ExchangeInfo info = getExchangeInfo();
        checkWebSocket(aggTradeWebSocket);
        aggTradeWebSocket = processAggTradeEvents(Objects.requireNonNull(info).getSymbols().stream()
                .filter(s -> (s.getSymbol().endsWith(BTC) || s.getSymbol().endsWith(USDT)) && SymbolStatus.TRADING.equals(s.getStatus()))
                .map(SymbolInfo::getSymbol)
                .collect(Collectors.toList()));
    }

    @Override
    @SneakyThrows
    public void initStatisticsWS() {
        ExchangeInfo info = getExchangeInfo();
   checkWebSocket(statisticsWebSocket);
             statisticsWebSocket = processStatisticEvents(Objects.requireNonNull(info).getSymbols().stream()
                .filter(s -> (s.getSymbol().endsWith(BTC) || s.getSymbol().endsWith(USDT)) && SymbolStatus.TRADING.equals(s.getStatus()))
                .map(SymbolInfo::getSymbol)
                .collect(Collectors.toSet()));
    }

    @Override
    public void initAbnormalTradingWS() {
        checkWebSocket(abnormalTradingWebSocket);
        abnormalTradingWebSocket = client.onAbnormalTradingNoticeEvent((AbnormalTradingNoticeEvent response) -> {
            AbnormalTrading abnormalTrading = mapper.map(response, AbnormalTrading.class);
            abnormalTradingService.save(abnormalTrading);
        });
    }

    private Closeable processKlineEvents(List<String> symbols) {
        return client.onCandlestickEvent(String.join(",", symbols).toLowerCase(), CandlestickInterval.ONE_MINUTE, (CandlestickEvent response) -> {
            Kline kline = mapper.map(response, Kline.class);
            addAndIncrementKline(kline);
            if (klineCounter.intValue() == batchSize) {
                insertAndDecrementKline(klines);
            }
        });
    }

    private Closeable processStatisticEvents(Set<String> symbols) {
        return client.onStatisticsEvent((List<StatisticsDto> response) -> {
            List<Statistics> statisticsList = new ArrayList<>();
            response.stream()
                    .filter(s -> symbols.contains(s.getSymbol()))
                    .forEach(s -> {
                        Statistics statistics = mapper.map(s, Statistics.class);
                        statisticsList.add(statistics);
                    });
            dataProcessorService.processStatistics(statisticsList);
            statisticsService.batchInsert(statisticsList);
        });
    }

    private ExchangeInfo getExchangeInfo() {
        return restTemplate.getForObject(BASE_API + EXCHANGE_INFO, ExchangeInfo.class);
    }

    @SneakyThrows
    private void checkWebSocket(Closeable closeable) {
        if (Objects.nonNull(closeable)) {
            closeable.close();
        }
    }

    private Closeable processAggTradeEvents(List<String> symbols) {
        return client.onAggTradeEvent(String.join(",", symbols).toLowerCase(), (AggTradeDto response) -> {
            AggTrade aggTrade = mapper.map(response, AggTrade.class);
            dataProcessorService.processAggTrades(aggTrade);
            //addAndIncrementAggTrade(aggTrade);
            //if (aggTradeCounter.intValue() == batchSize) {
            //    insertAndDecrementAggTrade(aggTrades);
            //}
        });
    }

    private synchronized int addAndIncrementKline(Kline kline) {
        klines.add(kline);
        return klineCounter.incrementAndGet();
    }

    private synchronized void insertAndDecrementKline(List<Kline> klines) {
        klineService.batchInsert(klines);
        klines.clear();
        klineCounter = new AtomicInteger();
    }

    private synchronized int addAndIncrementAggTrade(AggTrade aggTrade) {
        aggTrades.add(aggTrade);
        return aggTradeCounter.incrementAndGet();
    }

    private synchronized void insertAndDecrementAggTrade(List<AggTrade> aggTrades) {
        aggTradeService.batchInsert(aggTrades);
        aggTrades.clear();
        aggTradeCounter = new AtomicInteger();
    }
}
