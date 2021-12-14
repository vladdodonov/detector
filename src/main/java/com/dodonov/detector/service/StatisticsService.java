package com.dodonov.detector.service;

import com.dodonov.detector.entity.Statistics;

import java.util.List;

public interface StatisticsService {

    void batchInsert(List<Statistics> statistics);
}
