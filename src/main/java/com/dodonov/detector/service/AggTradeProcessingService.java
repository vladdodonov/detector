package com.dodonov.detector.service;

import com.dodonov.detector.entity.AggTrade;
import com.dodonov.detector.view.AggTradeProcessing;

import java.util.List;

public interface AggTradeProcessingService {
    List<AggTradeProcessing> saveAll(List<AggTradeProcessing> data);

    void batchInsert(List<AggTradeProcessing> data);
}
