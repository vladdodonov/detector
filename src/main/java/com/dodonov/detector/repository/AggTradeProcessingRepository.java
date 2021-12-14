package com.dodonov.detector.repository;

import com.dodonov.detector.view.AggTradeProcessing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AggTradeProcessingRepository extends JpaRepository<AggTradeProcessing, Long> {
}
