package com.dodonov.detector.service;

import com.dodonov.detector.entity.AbnormalTrading;
import com.dodonov.detector.entity.Kline;

import java.util.List;

public interface AbnormalTradingService {
    AbnormalTrading save(AbnormalTrading abnormalTrading);
}
