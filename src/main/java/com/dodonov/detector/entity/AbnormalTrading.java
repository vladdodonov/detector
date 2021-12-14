package com.dodonov.detector.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "abnormal_trading")
@Data
@NoArgsConstructor
public class AbnormalTrading {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_abnormal_trading")
    @SequenceGenerator(name = "seq_abnormal_trading", sequenceName = "seq_abnormal_trading", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "notice_type")
    private String noticeType;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "event_type")
    private String eventType;
    @Column(name = "volume")
    private BigDecimal volume;
    @Column(name = "price_change")
    private BigDecimal priceChange;
    @Column(name = "period")
    private String period;
    @Column(name = "send_timestamp")
    private LocalDateTime sendTimestamp;
    @Column(name = "baseAsset")
    private String baseAsset;
    @Column(name = "quotaAsset")
    private String quotaAsset;
}
