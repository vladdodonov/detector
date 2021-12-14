package com.dodonov.detector.service;

import com.dodonov.detector.entity.AggTrade;
import com.dodonov.detector.entity.Statistics;

import java.util.List;

public interface DataProcessorService {
    //void processKlines();

    void processAggTrades(AggTrade aggTrade);

    void processStatistics(List<Statistics> statistics);

    void clearStatisticsProcessingMap();
}
