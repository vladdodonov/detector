package com.dodonov.detector.repository;

import com.dodonov.detector.view.StatisticsProcessing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsProcessingRepository extends JpaRepository<StatisticsProcessing, Long> {
    Boolean existsBySymbolAndIsPumpIsTrue(String symbol);
}
