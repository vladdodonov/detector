package com.dodonov.detector.service.impl;

import com.dodonov.detector.repository.AggTradeProcessingRepository;
import com.dodonov.detector.repository.AggTradeRepository;
import com.dodonov.detector.service.AggTradeProcessingService;
import com.dodonov.detector.view.AggTradeProcessing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AggTradeProcessingServiceImpl implements AggTradeProcessingService {
    private final AggTradeProcessingRepository aggTradeProcessingRepository;
    private final JdbcTemplate template;
    @Override
    public List<AggTradeProcessing> saveAll(List<AggTradeProcessing> data) {
        return aggTradeProcessingRepository.saveAll(data);
    }

    @Override
    public void batchInsert(List<AggTradeProcessing> entities) {
        template.batchUpdate("insert into agg_trade_process (" +
                "event_time, " +
                "symbol, " +
                "buy_q, " +
                "sell_q, " +
                "buy_ratio " +
                ") values (?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setTimestamp(1, Timestamp.valueOf(entities.get(i).getEventTime()));
                ps.setString(2, entities.get(i).getSymbol());
                ps.setBigDecimal(3, entities.get(i).getBuyQuantity());
                ps.setBigDecimal(4, entities.get(i).getSellQuantity());
                ps.setBigDecimal(5, entities.get(i).getBuyRatio());
            }

            @Override
            public int getBatchSize() {
                return entities.size();
            }
        });
    }
}
