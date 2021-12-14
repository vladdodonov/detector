package com.dodonov.detector.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "kline")
@Data
@NoArgsConstructor
public class Kline {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_kline")
    @SequenceGenerator(name = "seq_kline", sequenceName = "seq_kline", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "event_time")
    private LocalDateTime eventTime;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "open_time")
    private LocalDateTime openTime;

    @Column(name = "open")
    private BigDecimal open;

    @Column(name = "high")
    private BigDecimal high;

    @Column(name = "low")
    private BigDecimal low;

    @Column(name = "close")
    private BigDecimal close;

    @Column(name = "volume")
    private BigDecimal volume;

    @Column(name = "close_time")
    private LocalDateTime closeTime;

    @Column(name = "interval_id")
    private String intervalId;

    @Column(name = "first_trade_id")
    private Long firstTradeId;

    @Column(name = "last_trade_id")
    private Long lastTradeId;

    @Column(name = "quote_asset_volume")
    private BigDecimal quoteAssetVolume;

    @Column(name = "number_of_trades")
    private Long numberOfTrades;

    @Column(name = "taker_buy_base_asset_volume")
    private BigDecimal takerBuyBaseAssetVolume;

    @Column(name = "taker_buy_quote_asset_volume")
    private BigDecimal takerBuyQuoteAssetVolume;

    @Column(name = "is_bar_final")
    private Boolean isBarFinal;
}
