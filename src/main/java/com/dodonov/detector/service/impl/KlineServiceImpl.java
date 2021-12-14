package com.dodonov.detector.service.impl;

import com.dodonov.detector.entity.Kline;
import com.dodonov.detector.repository.KlineRepository;
import com.dodonov.detector.service.KlineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KlineServiceImpl implements KlineService {
    private final KlineRepository klineRepository;
    private final JdbcTemplate template;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private Integer batchSize;

    @Override
    @Transactional
    public List<Kline> saveAll(List<Kline> aggTrades) {
        return klineRepository.saveAll(aggTrades);
    }

    @Override
    @Transactional
    public void batchInsert(List<Kline> entities) {
        template.batchUpdate("insert into kline (" +
                "event_type, " +
                "event_time, " +
                "symbol, " +
                "open_time, " +
                "open, " +
                "high, " +
                "low, " +
                "close, " +
                "volume, " +
                "close_time, " +
                "interval_id, " +
                "first_trade_id, " +
                "last_trade_id, " +
                "quote_asset_volume, " +
                "number_of_trades, " +
                "taker_buy_base_asset_volume, " +
                "taker_buy_quote_asset_volume, " +
                "is_bar_final" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, entities.get(i).getEventType());
                ps.setTimestamp(2, Timestamp.valueOf(entities.get(i).getEventTime()));
                ps.setString(3, entities.get(i).getSymbol());
                ps.setTimestamp(4, Timestamp.valueOf(entities.get(i).getOpenTime()));
                ps.setBigDecimal(5, entities.get(i).getOpen());
                ps.setBigDecimal(6, entities.get(i).getHigh());
                ps.setBigDecimal(7, entities.get(i).getLow());
                ps.setBigDecimal(8, entities.get(i).getClose());
                ps.setBigDecimal(9, entities.get(i).getVolume());
                ps.setTimestamp(10, Timestamp.valueOf(entities.get(i).getCloseTime()));
                ps.setString(11, entities.get(i).getIntervalId());
                ps.setLong(12, entities.get(i).getFirstTradeId());
                ps.setLong(13, entities.get(i).getLastTradeId());
                ps.setBigDecimal(14, entities.get(i).getQuoteAssetVolume());
                ps.setLong(15, entities.get(i).getNumberOfTrades());
                ps.setBigDecimal(16, entities.get(i).getTakerBuyBaseAssetVolume());
                ps.setBigDecimal(17, entities.get(i).getTakerBuyQuoteAssetVolume());
                ps.setBoolean(18, entities.get(i).getIsBarFinal());
            }

            @Override
            public int getBatchSize() {
                return batchSize;
            }
        });
    }
}