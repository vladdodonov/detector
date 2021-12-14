package com.dodonov.detector.repository;

import com.dodonov.detector.entity.PumpCheck;
import com.dodonov.detector.view.AggTradeProcessView;
import com.dodonov.detector.view.StatisticsProcessView;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Repository
@RequiredArgsConstructor
public class DataProcessorRepositoryImpl implements DataProcessorRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    private final JdbcTemplate template;


    @Override
    @Transactional(readOnly = true)
    public Set<AggTradeProcessView> processAggTrades(LocalDateTime startTimeFrame1Hour, LocalDateTime now) {
        System.out.println(startTimeFrame1Hour);
        System.out.println(now);
        return new HashSet<>(Optional.ofNullable((List<AggTradeProcessView>) entityManager.createNativeQuery("" +
                "with buys as (select sum(at.quantity) q, at.symbol s " +
                "              from agg_trade at " +
                "              where at.trade_time between :startTimeFrame1Hour and :now " +
                "                and is_buyer_maker is false " +
                "              group by s), " +
                "     sells as (select sum(at.quantity) q, at.symbol s " +
                "               from agg_trade at " +
                "               where at.trade_time between :startTimeFrame1Hour and :now " +
                "                 and is_buyer_maker is true " +
                "               group by s) " +
                "select b.s as symbol, b.q as buy_q, s.q as sell_q, b.q / s.q as ratio " +
                "from buys b " +
                "         inner join sells s on b.s = s.s where b.q / s.q >= 2.2")
                .unwrap(org.hibernate.query.Query.class)
                .setParameter("startTimeFrame1Hour", startTimeFrame1Hour)
                .setParameter("now", now)
                .setResultTransformer(AggTradeProcessView.transformer())
                .getResultList())
                .orElse(Collections.emptyList()));
    }

    @Override
    @Transactional
    public void batchInsertAggTradeProcessView(Set<AggTradeProcessView> set, LocalDateTime timeframe) {
        var entities = new ArrayList<>(set);
        template.batchUpdate("insert into agg_trade_process (" +
                "symbol, " +
                "event_time, " +
                "timeframe, " +
                "buy_q, " +
                "sell_q, " +
                "buy_ratio, " +
                "sell_ratio " +
                ") values (?, ?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, entities.get(i).getSymbol());
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(3, Timestamp.valueOf(timeframe));
                ps.setBigDecimal(4, entities.get(i).getBuyQuantity());
                ps.setBigDecimal(5, entities.get(i).getSellQuantity());
                ps.setBigDecimal(6, entities.get(i).getRatio());
            }

            @Override
            public int getBatchSize() {
                return entities.size();
            }
        });
    }

    @Override
    public Set<StatisticsProcessView> processStatistics(LocalDateTime startTimeFrame10Minutes, LocalDateTime now) {
        return new HashSet<>(
                entityManager.createNativeQuery("" +
                        "with " +
                        "     start_vals as ( " +
                        "         select min(s.last_price) lp, s.symbol " +
                        "         from statistics s " +
                        "         where s.event_time >= :startTimeFrame10Minutes " +
                        "         group by s.symbol), " +
                        "     last_vals as ( " +
                        "         select max(s.last_price) lp, s.symbol " +
                        "         from statistics s " +
                        "         where s.event_time >= :now " +
                        "         group by s.symbol), " +
                        "     start_id as (select max(s.id) id, s.symbol " +
                        "                  from statistics s " +
                        "                           inner join start_vals sv on sv.symbol = s.symbol " +
                        "                  where s.last_price = sv.lp and s.event_time >= :startTimeFrame10Minutes " +
                        "                  group by s.symbol), " +
                        "     last_id as (select max(s.id) id, s.symbol " +
                        "                 from statistics s " +
                        "                          inner join last_vals lv on lv.symbol = s.symbol " +
                        "                 where s.last_price = lv.lp and s.event_time >= :now " +
                        "                 group by s.symbol), " +
                        "     candidates as (select sv.symbol, ((lv.lp / sv.lp) - 1) as change, sv.lp as start_price, lv.lp as last_price, stid.id as startId, lid.id as lastId " +
                        "                    from start_vals sv " +
                        "                             inner join last_vals lv on lv.symbol = sv.symbol " +
                        "                             inner join start_id stid on sv.symbol = stid.symbol " +
                        "                             inner join last_id lid on lid.symbol = sv.symbol) " +
                        "select c.change, c.symbol, c.startId, c.lastId, :startTimeFrame10Minutes as stf " +
                        "from candidates c " +
                        "where c.change >= 0.1 and c.start_price > 0.000001")
                        .unwrap(org.hibernate.query.Query.class)
                        .setParameter("startTimeFrame10Minutes", startTimeFrame10Minutes)
                        .setParameter("now", now)
                        .setResultTransformer(StatisticsProcessView.transformer())
                        .getResultList());
    }

    @Override
    public List<String> checkBeforeInsert(List<String> symbols, LocalDateTime startTimeFrame10Minutes) {
        return Optional.ofNullable((List<String>) entityManager.createNativeQuery("select distinct sp.symbol " +
                "from statistics_process sp " +
                "where sp.symbol in (:symbols) " +
                "and sp.event_time >= :startTimeFrame")
                .setParameter("symbols", symbols)
                .setParameter("startTimeFrame", startTimeFrame10Minutes)
                .getResultList())
                .orElse(Collections.emptyList());
    }

    @Override
    @Transactional
    public void batchInsertStatisticsProcessView(Set<StatisticsProcessView> set) {
        if (set.isEmpty()) {
            return;
        }
        var anomalies = new ArrayList<>(set);
        template.batchUpdate("insert into statistics_process (" +
                "event_time, " +
                "symbol, " +
                "change, " +
                "start_id, " +
                "last_id," +
                "start_timeframe " +
                ") values (?, ?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(2, anomalies.get(i).getSymbol());
                ps.setBigDecimal(3, anomalies.get(i).getChange());
                ps.setLong(4, anomalies.get(i).getStartId());
                ps.setLong(5, anomalies.get(i).getLastId());
                ps.setTimestamp(6, Timestamp.valueOf(anomalies.get(i).getStartTimeFrame()));
            }

            @Override
            public int getBatchSize() {
                return anomalies.size();
            }
        });
    }

    @Override
    public Set<PumpCheck> checkPumps(Set<StatisticsProcessView> toInsert) {
        Set<PumpCheck> pumpChecks = new HashSet<>();
        LocalDateTime nowMinusThreeDays = LocalDateTime.now().minusDays(4);
        for (StatisticsProcessView v : toInsert) {
            Stream.ofNullable((List<PumpCheck>)entityManager.createNativeQuery("" +
                    "with abt as (select count(*) as abt from abnormal_trading ab " +
                    "                           where ab.symbol = :symbol and ab.send_timestamp >= :nowMinusThreeDays " +
                    "                                 and ab.event_type ilike 'HIGH_VOLUME_RISE_%' and ab.period = 'MINUTE_15'), " +

                    "     agt as (select count(*) as agt from agg_trade_process ag " +
                    "                           where ag.symbol = :symbol and ag.event_time >= :nowMinusThreeDays) " +
                    "select :symbol, abt.abt, agt.agt " +
                    "from agt, abt")
                    .unwrap(org.hibernate.query.Query.class)
                    .setParameter("nowMinusThreeDays", nowMinusThreeDays)
                    .setParameter("symbol", v.getSymbol())
                    .setResultTransformer(PumpCheck.transformer())
                    .getResultList())
                    .filter(Objects::nonNull)
                    .flatMap(Collection::stream)
                    .findFirst()
                    .ifPresent(pc -> {
                        pc.setEventTime(LocalDateTime.now());
                        pc.setTimeFrame(v.getStartTimeFrame());
                        pumpChecks.add(pc);
                    });
        }
        return pumpChecks;
    }

    @Override
    public boolean moreHasBoughtThenSold(String symbol) {
        return (Boolean) entityManager.createNativeQuery("" +
                "with cnt_buys as (select count(*) as c " +
                "from agg_trade_process atp " +
                "where atp.event_time between :before and :now " +
                "and atp.symbol = :symbol " +
                "and atp.buy_ratio >= 2.2)," +
                "cnt_sells as (select count(*) as c " +
                "from agg_trade_process atp " +
                "where atp.event_time between :before and :now " +
                "and atp.symbol = :symbol " +
                "and atp.buy_ratio <= 1.0)" +
                "select cb.c > cs.c from cnt_buys cb, cnt_sells cs")
                .setParameter("symbol", symbol)
                .setParameter("before", LocalDateTime.now().minus(4, ChronoUnit.DAYS))
                .setParameter("now", LocalDateTime.now())
                .getSingleResult();
    }

    @Override
    public Pair<Boolean, LocalDateTime> checkPump(String symbol, Double minPrice) {
        var result = (List<Object[]>) entityManager.createNativeQuery("" +
                "with start_pump as (select st.event_time as pump_start " +
                "                    from statistics st " +
                "                    where st.symbol = :symbol " +
                "                      and st.last_price <= :minPrice" +
                "                    order by st.id desc " +
                "                    limit 1) " +
                "select sum(atp.buy_q) / sum(atp.sell_q) >= 2.2 as isPump, sp.pump_start as pumpStart " +
                "from agg_trade_process atp, " +
                "     start_pump sp " +
                "where atp.event_time between :before and sp.pump_start " +
                "  and atp.symbol = :symbol ")
                .setParameter("symbol", symbol)
                .setParameter("before", LocalDateTime.now().minus(3, ChronoUnit.DAYS))
                .setParameter("minPrice", minPrice)
                .getResultList();
        return Pair.of((Boolean) result.get(0)[0], (LocalDateTime) result.get(0)[1]);
    }
}
