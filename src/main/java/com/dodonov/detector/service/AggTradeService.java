package com.dodonov.detector.service;

import com.dodonov.detector.entity.AggTrade;
import com.dodonov.detector.entity.Kline;

import java.util.List;

public interface AggTradeService {
    List<AggTrade> saveAll(List<AggTrade> klines);

    void batchInsert(List<AggTrade> aggTrades);
}
