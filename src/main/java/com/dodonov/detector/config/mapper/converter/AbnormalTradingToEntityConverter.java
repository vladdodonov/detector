package com.dodonov.detector.config.mapper.converter;

import com.dodonov.detector.binance.api.client.domain.event.AbnormalTradingNoticeEvent;
import com.dodonov.detector.config.mapper.MapperConfigurer;
import com.dodonov.detector.entity.AbnormalTrading;
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
public class AbnormalTradingToEntityConverter implements MapperConfigurer {
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
                .createTypeMap(AbnormalTradingNoticeEvent.class, AbnormalTrading.class)
                .addMappings(new PropertyMap<>() {
                    @Override
                    protected void configure() {
                        using(stringToBigDecimalConverter).map(source.getData().getPriceChange(), destination.getPriceChange());
                        using(stringToBigDecimalConverter).map(source.getData().getVolume(), destination.getVolume());
                        using(longLocalDateTimeConverter).map(source.getData().getSendTimestamp(), destination.getSendTimestamp());
                        map(source.getData().getNoticeType(), destination.getNoticeType());
                        map(source.getData().getSymbol(), destination.getSymbol());
                        map(source.getData().getEventType(), destination.getEventType());
                        map(source.getData().getPeriod(), destination.getPeriod());
                        map(source.getData().getBaseAsset(), destination.getBaseAsset());
                        map(source.getData().getQuotaAsset(), destination.getQuotaAsset());
                    }
                });
    }
}
