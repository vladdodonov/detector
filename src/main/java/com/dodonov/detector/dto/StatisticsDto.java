package com.dodonov.detector.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StatisticsDto {
    private Long id;
    @JsonProperty("s")
    private String symbol;
    @JsonProperty("p")
    private String priceChange;
    @JsonProperty("P")
    private String priceChangePercent;
    @JsonProperty("w")
    private String weightedAvgPrice;
    private String prevClosePrice;
    @JsonProperty("c")
    private String lastPrice;
    @JsonProperty("Q")
    private String lastQty;
    @JsonProperty("b")
    private String bidPrice;
    @JsonProperty("B")
    private String bidQty;
    @JsonProperty("a")
    private String askPrice;
    @JsonProperty("A")
    private String askQty;
    @JsonProperty("o")
    private String openPrice;
    @JsonProperty("h")
    private String highPrice;
    @JsonProperty("l")
    private String lowPrice;
    @JsonProperty("v")
    private String volume;
    @JsonProperty("q")
    private String quoteVolume;
    @JsonProperty("O")
    private Long openTime;
    @JsonProperty("C")
    private Long closeTime;
    @JsonProperty("F")
    private Long firstId;
    @JsonProperty("L")
    private Long lastId;
    @JsonProperty("n")
    private Long count;
}
