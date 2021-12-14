package com.dodonov.detector.repository;

import com.dodonov.detector.entity.PumpCheck;
import com.dodonov.detector.view.AggTradeProcessView;
import com.dodonov.detector.view.StatisticsProcessView;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface DataProcessorRepository {

    Set<AggTradeProcessView> processAggTrades(LocalDateTime startTimeFrame1Hour, LocalDateTime now);

    void batchInsertAggTradeProcessView(Set<AggTradeProcessView> set, LocalDateTime timeframe);

    void batchInsertStatisticsProcessView(Set<StatisticsProcessView> set);

    Set<StatisticsProcessView> processStatistics(LocalDateTime startTimeFrame10Minutes, LocalDateTime now);

    List<String> checkBeforeInsert(List<String> symbols, LocalDateTime startTimeFrame10Minutes);

    Set<PumpCheck> checkPumps(Set<StatisticsProcessView> toInsert);

    boolean moreHasBoughtThenSold(String symbol);

    Pair<Boolean, LocalDateTime> checkPump(String symbol, Double minPrice);
}
