package com.dodonov.detector.service;

public interface WebSocketService {
    void initKlinesWS();

    void initAggTradesWS();

    void initStatisticsWS();

    void initAbnormalTradingWS();
}
