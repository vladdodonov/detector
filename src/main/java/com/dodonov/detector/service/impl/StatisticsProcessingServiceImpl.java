package com.dodonov.detector.service.impl;

import com.dodonov.detector.repository.StatisticsProcessingRepository;
import com.dodonov.detector.service.StatisticsProcessingService;
import com.dodonov.detector.view.StatisticsProcessing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatisticsProcessingServiceImpl implements StatisticsProcessingService {
    private final StatisticsProcessingRepository repo;

    public StatisticsProcessingServiceImpl(StatisticsProcessingRepository repo) {
        this.repo = repo;
    }

    @Override
    public StatisticsProcessing save(StatisticsProcessing sp) {
        return repo.save(sp);
    }

    @Override
    public Boolean wasPumped(String symbol) {
        return repo.existsBySymbolAndIsPumpIsTrue(symbol);
    }
}
