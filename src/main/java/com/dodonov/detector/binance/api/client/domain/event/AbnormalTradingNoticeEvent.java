package com.dodonov.detector.binance.api.client.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AbnormalTradingNoticeEvent {

  private String stream;

  private Data data;

  public String getStream() {
    return stream;
  }

  public void setStream(String stream) {
    this.stream = stream;
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Data {
    private String noticeType;
    private String symbol;
    private String eventType;
    private String volume;
    private Long sendTimestamp;
    private String baseAsset;
    private String quotaAsset;
    private String priceChange;
    private String period;

    public String getNoticeType() {
      return noticeType;
    }

    public void setNoticeType(String noticeType) {
      this.noticeType = noticeType;
    }

    public String getSymbol() {
      return symbol;
    }

    public void setSymbol(String symbol) {
      this.symbol = symbol;
    }

    public String getEventType() {
      return eventType;
    }

    public void setEventType(String eventType) {
      this.eventType = eventType;
    }

    public String getVolume() {
      return volume;
    }

    public void setVolume(String volume) {
      this.volume = volume;
    }

    public long getSendTimestamp() {
      return sendTimestamp;
    }

    public void setSendTimestamp(long sendTimestamp) {
      this.sendTimestamp = sendTimestamp;
    }

    public String getBaseAsset() {
      return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
      this.baseAsset = baseAsset;
    }

    public String getQuotaAsset() {
      return quotaAsset;
    }

    public void setQuotaAsset(String quotaAsset) {
      this.quotaAsset = quotaAsset;
    }

    public void setSendTimestamp(Long sendTimestamp) {
      this.sendTimestamp = sendTimestamp;
    }

    public String getPriceChange() {
      return priceChange;
    }

    public void setPriceChange(String priceChange) {
      this.priceChange = priceChange;
    }

    public String getPeriod() {
      return period;
    }

    public void setPeriod(String period) {
      this.period = period;
    }
  }
}
