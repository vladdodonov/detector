package com.dodonov.detector.config.mapper.converter;

import com.dodonov.detector.config.mapper.MapperConfigurer;
import com.dodonov.detector.dto.StatisticsDto;
import com.dodonov.detector.entity.Statistics;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Component
public class StatisticsDtoToStatisticsConverter implements MapperConfigurer {
    Converter<Long, LocalDateTime> longLocalDateTimeConverter =
            context -> Instant
                    .ofEpochMilli(context.getSource())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
    Converter<String, BigDecimal> stringToBigDecimalConverter =
            context -> Optional.ofNullable(context.getSource())
                    .map(BigDecimal::new)
                    .orElse(null);

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper
                .createTypeMap(StatisticsDto.class, Statistics.class)
                .addMappings(new PropertyMap<>() {
                    @Override
                    protected void configure() {
                        map(source.getSymbol(), destination.getSymbol());
                        using(stringToBigDecimalConverter).map(source.getPriceChange(), destination.getPriceChange());
                        using(stringToBigDecimalConverter).map(source.getPriceChangePercent(), destination.getPriceChangePercent());
                        using(stringToBigDecimalConverter).map(source.getWeightedAvgPrice(), destination.getWeightedAvgPrice());
                        using(stringToBigDecimalConverter).map(source.getPrevClosePrice(), destination.getPrevClosePrice());
                        using(stringToBigDecimalConverter).map(source.getLastPrice(), destination.getLastPrice());
                        using(stringToBigDecimalConverter).map(source.getLastQty(), destination.getLastQty());
                        using(stringToBigDecimalConverter).map(source.getBidPrice(), destination.getBidPrice());
                        using(stringToBigDecimalConverter).map(source.getBidQty(), destination.getBidQty());
                        using(stringToBigDecimalConverter).map(source.getAskPrice(), destination.getAskPrice());
                        using(stringToBigDecimalConverter).map(source.getAskQty(), destination.getAskQty());
                        using(stringToBigDecimalConverter).map(source.getOpenPrice(), destination.getOpenPrice());
                        using(stringToBigDecimalConverter).map(source.getHighPrice(), destination.getHighPrice());
                        using(stringToBigDecimalConverter).map(source.getLowPrice(), destination.getLowPrice());
                        using(stringToBigDecimalConverter).map(source.getVolume(), destination.getVolume());
                        using(stringToBigDecimalConverter).map(source.getQuoteVolume(), destination.getQuoteVolume());
                        using(longLocalDateTimeConverter).map(source.getOpenTime(), destination.getOpenTime());
                        using(longLocalDateTimeConverter).map(source.getCloseTime(), destination.getCloseTime());
                        map(source.getFirstId(), destination.getFirstTradeId());
                        map(source.getLastId(), destination.getLastTradeId());
                        map(source.getCount(), destination.getCount());
                    }
                });
    }

}
