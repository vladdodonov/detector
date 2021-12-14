package com.dodonov.detector.view;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;

@Data
@Entity
@Table(name = "statistics_process")
public class StatisticsProcessing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_statistics_process")
    @SequenceGenerator(name = "seq_statistics_process", sequenceName = "seq_statistics_process", allocationSize = 1)
    private Long id;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "event_time")
    private LocalDateTime eventTime;
    @Column(name = "low")
    private BigDecimal low;
    @Column(name = "high")
    private BigDecimal high;
    @Column(name = "ratio")
    private BigDecimal ratio;
    @Column(name = "is_pump")
    private Boolean isPump;
    @Column(name = "pump_start")
    private LocalDateTime pumpStartTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticsProcessing that = (StatisticsProcessing) o;
        return Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public String toString() {
        return "StatisticsProcessing{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", eventTime=" + eventTime +
                ", low=" + low +
                ", high=" + high +
                ", ratio=" + ratio +
                ", isPump=" + isPump +
                '}';
    }
}
