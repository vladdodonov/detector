package com.dodonov.detector.config.mapper.converter;

import com.dodonov.detector.config.mapper.MapperConfigurer;
import com.dodonov.detector.dto.AggTradeDto;
import com.dodonov.detector.entity.AggTrade;
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
public class AggTradeDtoToEntityConverter implements MapperConfigurer {
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
                .createTypeMap(AggTradeDto.class, AggTrade.class)
                .addMappings(new PropertyMap<>() {
                    @Override
                    protected void configure() {
                        using(stringToBigDecimalConverter).map(source.getPrice(), destination.getPrice());
                        using(stringToBigDecimalConverter).map(source.getQuantity(), destination.getQuantity());
                        using(longLocalDateTimeConverter).map(source.getEventTime(), destination.getEventTime());
                        using(longLocalDateTimeConverter).map(source.getTradeTime(), destination.getTradeTime());
                        map(source.getIsBuyerMaker(), destination.getIsBuyerMaker());
                    }
                });
    }
}
