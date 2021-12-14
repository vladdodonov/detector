package com.dodonov.detector.service;

import com.dodonov.detector.view.StatisticsProcessing;

public interface StatisticsProcessingService {
    StatisticsProcessing save(StatisticsProcessing sp);

    Boolean wasPumped(String symbol);
}
