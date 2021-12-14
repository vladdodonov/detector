package com.dodonov.detector.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "agg_trade")
@Data
@NoArgsConstructor
public class AggTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_agg_trade")
    @SequenceGenerator(name = "seq_agg_trade", sequenceName = "seq_agg_trade", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "event_time")
    private LocalDateTime eventTime;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "trade_time")
    private LocalDateTime tradeTime;
    @Column(name = "is_buyer_maker")
    private Boolean isBuyerMaker;
}
