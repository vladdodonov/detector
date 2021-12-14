package com.dodonov.detector.repository;

import com.dodonov.detector.entity.AggTrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AggTradeRepository extends JpaRepository<AggTrade, Long> {
}
