package com.dodonov.detector.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Constant {
    public static final String BASE_API = "https://api.binance.com/api";
    public static final String EXCHANGE_INFO = "/v1/exchangeInfo";
    public static final String BTC = "BTC";
    public static final String USDT = "USDT";
    public static final String EVERY_HALF_HOUR = "0 0/30 * * * *";
}
