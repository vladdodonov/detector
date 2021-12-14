package com.dodonov.detector.config.ws;

import com.dodonov.detector.binance.api.client.BinanceApiClientFactory;
import com.dodonov.detector.binance.api.client.BinanceApiWebSocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig {
    @Bean
    public BinanceApiWebSocketClient binanceApiWebSocketClient() {
        return BinanceApiClientFactory.newInstance().newWebSocketClient();
    }
}
