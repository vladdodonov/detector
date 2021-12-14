package com.dodonov.detector.entity;

import com.dodonov.detector.view.AggTradeProcessView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.transform.ResultTransformer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "pump_check")
@Data
@NoArgsConstructor
public class PumpCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pump_check")
    @SequenceGenerator(name = "seq_pump_check", sequenceName = "seq_pump_check", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "event_time")
    private LocalDateTime eventTime;
    @Column(name = "start_timeframe")
    private LocalDateTime timeFrame;
    @Column(name = "abnormal_trading")
    private Long areAbnormalTradingsDetected;
    @Column(name = "huge_volumes")
    private Long areHugeBuyVolumesDetected;

    public PumpCheck(String symbol, BigInteger areAbnormalTradingsDetected, BigInteger areHugeBuyVolumesDetected) {
        this.symbol = symbol;
        this.areAbnormalTradingsDetected = areAbnormalTradingsDetected.longValue();
        this.areHugeBuyVolumesDetected = areHugeBuyVolumesDetected.longValue();
    }

    public static ResultTransformer transformer() {
        return new PumpCheck.PumpCheckResultTransformer();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PumpCheck pumpCheck = (PumpCheck) o;
        return Objects.equals(symbol, pumpCheck.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    private static class PumpCheckResultTransformer implements ResultTransformer {

        @Override
        public Object transformTuple(Object[] objects, String[] strings) {
            return new PumpCheck(
                    (String) objects[0],
                    (BigInteger) objects[1],
                    (BigInteger) objects[2]
            );
        }

        @Override
        public List transformList(List list) {
            return list;
        }
    }
}
