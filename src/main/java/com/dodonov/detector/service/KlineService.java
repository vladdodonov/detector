package com.dodonov.detector.service;

import com.dodonov.detector.entity.Kline;

import java.util.List;

public interface KlineService {
    List<Kline> saveAll(List<Kline> klines);

    void batchInsert(List<Kline> aggTrades);
}
