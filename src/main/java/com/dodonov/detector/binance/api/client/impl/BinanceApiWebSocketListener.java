package com.dodonov.detector.binance.api.client.impl;

import com.dodonov.detector.binance.api.client.BinanceApiCallback;
import com.dodonov.detector.binance.api.client.exception.BinanceApiException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import java.io.IOException;

import static com.dodonov.detector.binance.api.client.impl.BinanceApiServiceGenerator.getSharedClient;

/**
 * Binance API WebSocket listener.
 */
@Slf4j
public class BinanceApiWebSocketListener<T> extends WebSocketListener {

    private final BinanceApiCallback<T> callback;

    private Class<T> eventClass;

    private TypeReference<T> eventTypeReference;

    private boolean closing = false;

    public BinanceApiWebSocketListener(BinanceApiCallback<T> callback, Class<T> eventClass) {
        this.callback = callback;
        this.eventClass = eventClass;
    }

    public BinanceApiWebSocketListener(BinanceApiCallback<T> callback, TypeReference<T> eventTypeReference) {
        this.callback = callback;
        this.eventTypeReference = eventTypeReference;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            T event = null;
            if (eventClass == null) {
                event = mapper.readValue(text, eventTypeReference);
            } else {
                event = mapper.readValue(text, eventClass);
            }
            callback.onResponse(event);
        } catch (IOException e) {
            throw new BinanceApiException(e);
        }
    }

    @Override
    public void onClosing(final WebSocket webSocket, final int code, final String reason) {
        closing = true;
        log.error("ЗАКРЫВАЕМСЯ");
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        if (!closing) {
            log.error(t.getMessage());
            log.error("ОШИБКА");
            getSharedClient().newWebSocket(webSocket.request(), this);
            t.printStackTrace();
        }
    }
}