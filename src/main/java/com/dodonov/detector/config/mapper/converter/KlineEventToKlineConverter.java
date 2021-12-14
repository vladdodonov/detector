package com.dodonov.detector.config.mapper.converter;

import com.dodonov.detector.binance.api.client.domain.event.CandlestickEvent;
import com.dodonov.detector.config.mapper.MapperConfigurer;
import com.dodonov.detector.entity.Kline;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class KlineEventToKlineConverter implements MapperConfigurer {
    Converter<Long, LocalDateTime> longLocalDateTimeConverter =
            context -> Instant
                    .ofEpochMilli(context.getSource())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
    Converter<String, BigDecimal> stringToBigDecimalConverter =
            context -> new BigDecimal(context.getSource());

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper
                .createTypeMap(CandlestickEvent.class, Kline.class)
                .addMappings(new PropertyMap<>() {
                    @Override
                    protected void configure() {
                        map(source.getEventType(), destination.getEventType());
                        using(longLocalDateTimeConverter).map(source.getEventTime(), destination.getEventTime());
                        map(source.getSymbol(), destination.getSymbol());
                        using(longLocalDateTimeConverter).map(source.getOpenTime(), destination.getOpenTime());
                        using(stringToBigDecimalConverter).map(source.getOpen(), destination.getOpen());
                        using(stringToBigDecimalConverter).map(source.getHigh(), destination.getHigh());
                        using(stringToBigDecimalConverter).map(source.getLow(), destination.getLow());
                        using(stringToBigDecimalConverter).map(source.getClose(), destination.getClose());
                        using(stringToBigDecimalConverter).map(source.getVolume(), destination.getVolume());
                        using(longLocalDateTimeConverter).map(source.getCloseTime(), destination.getCloseTime());
                        map(source.getIntervalId(), destination.getIntervalId());
                        map(source.getFirstTradeId(), destination.getFirstTradeId());
                        map(source.getLastTradeId(), destination.getLastTradeId());
                        using(stringToBigDecimalConverter).map(source.getQuoteAssetVolume(), destination.getQuoteAssetVolume());
                        map(source.getNumberOfTrades(), destination.getNumberOfTrades());
                        using(stringToBigDecimalConverter).map(source.getTakerBuyBaseAssetVolume(), destination.getTakerBuyBaseAssetVolume());
                        using(stringToBigDecimalConverter).map(source.getTakerBuyQuoteAssetVolume(), destination.getTakerBuyQuoteAssetVolume());
                        map(source.getBarFinal(), destination.getIsBarFinal());
                    }
                });
    }

}
