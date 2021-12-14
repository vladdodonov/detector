package com.dodonov.detector.service.impl;

import com.dodonov.detector.entity.AggTrade;
import com.dodonov.detector.entity.Statistics;
import com.dodonov.detector.repository.DataProcessorRepository;
import com.dodonov.detector.service.*;
import com.dodonov.detector.view.AggTradeProcessing;
import com.dodonov.detector.view.StatisticsProcessing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataProcessorServiceImpl implements DataProcessorService {
    private final AggTradeProcessingService aggTradeProcessingService;
    private final StatisticsProcessingService statisticsProcessingService;
    private final DataProcessorRepository dataProcessorRepository;
    private final ConcurrentHashMap<String, AggTradeProcessing> aggTradeProcessingMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Statistics>> statisticsMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> statisticsProcessingMap = new ConcurrentHashMap<>();
    private LocalDateTime now = LocalDateTime.now();
    private LocalDateTime startTime = LocalDateTime.now();

    /**
     * Данный метод ежечасно вставляет в agg_trade_processing валюты 2.2 и 0.4
     * @param aggTrade
     */
    @Override
    public void processAggTrades(AggTrade aggTrade) {
        AggTradeProcessing data;
        if (aggTradeProcessingMap.get(aggTrade.getSymbol()) != null) {
            data = aggTradeProcessingMap.get(aggTrade.getSymbol());
        } else {
            data = new AggTradeProcessing();
            data.setSymbol(aggTrade.getSymbol());
            data.setSellQuantity(new BigDecimal("0.0"));
            data.setBuyQuantity(new BigDecimal("0.0"));
            aggTradeProcessingMap.put(aggTrade.getSymbol(), data);
        }
        data.setEventTime(LocalDateTime.now());
        if (aggTrade.getIsBuyerMaker()) {
            data.setSellQuantity(data.getSellQuantity().add(aggTrade.getQuantity()));
        } else {
            data.setBuyQuantity(data.getBuyQuantity().add(aggTrade.getQuantity()));
        }
        data.setEventTime(LocalDateTime.now());
        data.setBuyRatio(data.getBuyQuantity().divide(data.getSellQuantity().equals(new BigDecimal("0.0")) ? new BigDecimal("1.0") : data.getSellQuantity(), RoundingMode.CEILING));
        data.setEventTime(LocalDateTime.now());
        //if (now.until(LocalDateTime.now(), ChronoUnit.MINUTES) >= 60) {
        //    now = LocalDateTime.now();
        //    aggTradeProcessingService.batchInsert(aggTradeProcessingMap.values().stream()
        //            .filter(a -> a.getBuyRatio().compareTo(new BigDecimal("2.2")) >= 0)
        //            .collect(Collectors.toList()));
        //    aggTradeProcessingService.batchInsert(aggTradeProcessingMap.values().stream()
        //            .filter(a -> a.getBuyRatio().compareTo(new BigDecimal("0.4")) <= 0)
        //            .collect(Collectors.toList()));
        //    aggTradeProcessingMap.clear();
        //}
        if (now.until(LocalDateTime.now(), ChronoUnit.MINUTES) >= 1) {
            now = LocalDateTime.now();
            aggTradeProcessingService.batchInsert(new ArrayList<>(aggTradeProcessingMap.values()));
            aggTradeProcessingService.batchInsert(new ArrayList<>(aggTradeProcessingMap.values()));
            aggTradeProcessingMap.clear();
        }
    }

    /**
     * В данном методе реализован таймфрейм 10 минут
     * @param statistics
     */
    @Override
    public void processStatistics(List<Statistics> statistics) {
                statistics.parallelStream()
                .forEach(s -> {
                    ConcurrentLinkedQueue<Statistics> queue;
                    if (statisticsMap.get(s.getSymbol()) != null) {
                        queue = statisticsMap.get(s.getSymbol());
                        if (queue.size() >= 600) {
                            queue.poll();
                        }
                    } else {
                        queue = new ConcurrentLinkedQueue<>();
                        statisticsMap.put(s.getSymbol(), queue);
                    }
                    queue.add(s);
                    processStatisticsSymbol(s.getSymbol());
                });
    }

    @Async
    void processStatisticsSymbol(String symbol) {
        var queue = statisticsMap.get(symbol);
        var low = queue.stream()
                .map(Statistics::getLastPrice)
                .min(Comparator.naturalOrder())
                .orElseThrow(RuntimeException::new);
        var high = queue.stream()
                .map(Statistics::getLastPrice)
                .max(Comparator.naturalOrder())
                .orElseThrow(RuntimeException::new);
        var processing = new StatisticsProcessing();
        processing.setSymbol(symbol);
        processing.setHigh(high);
        processing.setLow(low);
        processing.setRatio(high.divide(low, RoundingMode.CEILING));
        processing.setEventTime(LocalDateTime.now());
        if (startTime.until(LocalDateTime.now(), ChronoUnit.DAYS) >= 3) { // Нагуливаем данные за несколько дней об объемах
            if (processing.getRatio().compareTo(new BigDecimal("1.05")) >= 0) {
                if (statisticsProcessingMap.get(processing.getSymbol()) == null
                        || statisticsProcessingMap.get(processing.getSymbol()).until(LocalDateTime.now(), ChronoUnit.MINUTES) >= 11) {
                    //processing.setIsPump(dataProcessorRepository.moreHasBoughtThenSold(symbol));
                    var wasPumped = statisticsProcessingService.wasPumped(symbol);
                    if (!wasPumped) { // Тут надо сделать логику, которая не учитывает объемы во время пампа и т д
                        var isPump = dataProcessorRepository.checkPump(symbol, low.doubleValue());
                        if (BooleanUtils.isTrue(isPump.getLeft())) {
                            processing.setIsPump(true);
                            processing.setPumpStartTime(isPump.getRight());
                        } else {
                            processing.setIsPump(false);
                        }
                        statisticsProcessingService.save(processing);
                        statisticsProcessingMap.put(processing.getSymbol(), LocalDateTime.now());
                    }
                }
            }
        }
    }

    @Override
    public void clearStatisticsProcessingMap(){
        statisticsProcessingMap.forEach((k, v) ->{
            if (v.until(LocalDateTime.now(), ChronoUnit.MINUTES) >= 11){
                statisticsProcessingMap.remove(k);
            }
        });
    }
}