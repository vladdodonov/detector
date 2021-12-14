package com.dodonov.detector.service.impl;

import com.dodonov.detector.entity.Statistics;
import com.dodonov.detector.repository.StatisticsRepository;
import com.dodonov.detector.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final JdbcTemplate template;
    private final StatisticsRepository statisticsRepository;


    @Override
    @Transactional
    public void batchInsert(List<Statistics> entities) {
        template.batchUpdate("insert into statistics (" +
                "event_time, " +
                "symbol, " +
                "price_change, " +
                "price_change_percent, " +
                "weighted_avg_price, " +
                "prev_close_price, " +
                "last_price, " +
                "last_qty, " +
                "bid_price, " +
                "bid_qty, " +
                "ask_price, " +
                "ask_qty, " +
                "open_price, " +
                "high_price, " +
                "low_price, " +
                "volume, " +
                "quote_volume, " +
                "open_time, " +
                "close_time, " +
                "first_trade_id, " +
                "last_trade_id, " +
                "count " +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(2, entities.get(i).getSymbol());
                ps.setBigDecimal(3, entities.get(i).getPriceChange());
                ps.setBigDecimal(4, entities.get(i).getPriceChangePercent());
                ps.setBigDecimal(5, entities.get(i).getWeightedAvgPrice());
                ps.setBigDecimal(6, entities.get(i).getPrevClosePrice());
                ps.setBigDecimal(7, entities.get(i).getLastPrice());
                ps.setBigDecimal(8, entities.get(i).getLastQty());
                ps.setBigDecimal(9, entities.get(i).getBidPrice());
                ps.setBigDecimal(10, entities.get(i).getBidQty());
                ps.setBigDecimal(11, entities.get(i).getAskPrice());
                ps.setBigDecimal(12, entities.get(i).getAskQty());
                ps.setBigDecimal(13, entities.get(i).getOpenPrice());
                ps.setBigDecimal(14, entities.get(i).getHighPrice());
                ps.setBigDecimal(15, entities.get(i).getLowPrice());
                ps.setBigDecimal(16, entities.get(i).getVolume());
                ps.setBigDecimal(17, entities.get(i).getQuoteVolume());
                ps.setTimestamp(18, Timestamp.valueOf(entities.get(i).getOpenTime()));
                ps.setTimestamp(19, Timestamp.valueOf(entities.get(i).getCloseTime()));
                ps.setLong(20, entities.get(i).getFirstTradeId());
                ps.setLong(21, entities.get(i).getLastTradeId());
                ps.setLong(22, entities.get(i).getCount());
            }

            @Override
            public int getBatchSize() {
                return entities.size();
            }
        });
    }
}