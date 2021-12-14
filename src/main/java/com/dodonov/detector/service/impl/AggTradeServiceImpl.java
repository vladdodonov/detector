package com.dodonov.detector.service.impl;

import com.dodonov.detector.entity.AggTrade;
import com.dodonov.detector.repository.AggTradeRepository;
import com.dodonov.detector.service.AggTradeService;
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
public class AggTradeServiceImpl implements AggTradeService {
    private final AggTradeRepository aggTradeRepository;
    private final JdbcTemplate template;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private Integer batchSize;

    @Override
    @Transactional
    public List<AggTrade> saveAll(List<AggTrade> aggTrades) {
        return aggTradeRepository.saveAll(aggTrades);
    }

    @Override
    @Transactional
    public void batchInsert(List<AggTrade> entities) {
        template.batchUpdate("insert into agg_trade (" +
                "event_time, " +
                "symbol, " +
                "price, " +
                "quantity, " +
                "trade_time, " +
                "is_buyer_maker " +
                ") values (?, ?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setTimestamp(1, Timestamp.valueOf(entities.get(i).getEventTime()));
                ps.setString(2, entities.get(i).getSymbol());
                ps.setBigDecimal(3, entities.get(i).getPrice());
                ps.setBigDecimal(4, entities.get(i).getQuantity());
                ps.setTimestamp(5, Timestamp.valueOf(entities.get(i).getTradeTime()));
                ps.setBoolean(6, entities.get(i).getIsBuyerMaker());
            }

            @Override
            public int getBatchSize() {
                return batchSize;
            }
        });
    }
}