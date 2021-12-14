package com.dodonov.detector.repository;

import com.dodonov.detector.entity.AbnormalTrading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbnormalTradingRepository extends JpaRepository<AbnormalTrading, Long> {
}
