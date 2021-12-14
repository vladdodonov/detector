package com.dodonov.detector.service.impl;

import com.dodonov.detector.entity.AbnormalTrading;
import com.dodonov.detector.repository.AbnormalTradingRepository;
import com.dodonov.detector.service.AbnormalTradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AbnormalTradingServiceImpl implements AbnormalTradingService {
    private final AbnormalTradingRepository abnormalTradingRepository;

    @Override
    @Transactional
    public AbnormalTrading save(AbnormalTrading abnormalTrading) {
        return abnormalTradingRepository.save(abnormalTrading);
    }
}
