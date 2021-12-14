package com.dodonov.detector.repository;

import com.dodonov.detector.entity.Kline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KlineRepository extends JpaRepository<Kline, Long> {

}
