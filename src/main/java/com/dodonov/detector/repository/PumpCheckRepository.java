package com.dodonov.detector.repository;

import com.dodonov.detector.entity.PumpCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PumpCheckRepository extends JpaRepository<PumpCheck, Long> {
}
