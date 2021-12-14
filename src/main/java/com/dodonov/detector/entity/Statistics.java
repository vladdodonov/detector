package com.dodonov.detector.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "statistics")
@Data
@NoArgsConstructor
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_statistics")
    @SequenceGenerator(name = "seq_statistics", sequenceName = "seq_statistics", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "price_change")
    private BigDecimal priceChange;

    @Column(name = "price_change_percent")
    private BigDecimal priceChangePercent;

    @Column(name = "weighted_avg_price")
    private BigDecimal weightedAvgPrice;

    @Column(name = "prev_close_price")
    private BigDecimal prevClosePrice;

    @Column(name = "last_price")
    private BigDecimal lastPrice;

    @Column(name = "last_qty")
    private BigDecimal lastQty;

    @Column(name = "bid_price")
    private BigDecimal bidPrice;

    @Column(name = "bid_qty")
    private BigDecimal bidQty;

    @Column(name = "ask_price")
    private BigDecimal askPrice;

    @Column(name = "ask_qty")
    private BigDecimal askQty;

    @Column(name = "open_price")
    private BigDecimal openPrice;

    @Column(name = "high_price")
    private BigDecimal highPrice;

    @Column(name = "low_price")
    private BigDecimal lowPrice;

    @Column(name = "volume")
    private BigDecimal volume;

    @Column(name = "quote_volume")
    private BigDecimal quoteVolume;

    @Column(name = "open_time")
    private LocalDateTime openTime;

    @Column(name = "close_time")
    private LocalDateTime closeTime;

    @Column(name = "first_trade_id")
    private Long firstTradeId;

    @Column(name = "last_trade_id")
    private Long lastTradeId;

    @Column(name = "count")
    private Long count;

    @Column(name = "event_time")
    private LocalDateTime eventTime = LocalDateTime.now();
}
