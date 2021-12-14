package com.dodonov.detector.view;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "agg_trade_process")
public class AggTradeProcessing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_agg_trade_process")
    @SequenceGenerator(name = "seq_agg_trade_process", sequenceName = "seq_agg_trade_process", allocationSize = 1)
    private Long id;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "event_time")
    private LocalDateTime eventTime;
    @Column(name = "buy_q")
    private BigDecimal buyQuantity;
    @Column(name = "sell_q")
    private BigDecimal sellQuantity;
    @Column(name = "buy_ratio")
    private BigDecimal buyRatio;

    public AggTradeProcessing() {
    }

    public AggTradeProcessing(String symbol, BigDecimal buyQuantity, BigDecimal sellQuantity, BigDecimal buyRatio) {
        this.symbol = symbol;
        this.buyQuantity = buyQuantity;
        this.sellQuantity = sellQuantity;
        this.buyRatio = buyRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggTradeProcessing that = (AggTradeProcessing) o;
        return Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
