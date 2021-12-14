package com.dodonov.detector.job;

import com.dodonov.detector.service.DataProcessorService;
import com.dodonov.detector.service.WebSocketService;
import com.dodonov.detector.util.annotation.PostInitialize;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.dodonov.detector.util.Constant.EVERY_HALF_HOUR;

@Service
@RequiredArgsConstructor
public class Job {
    private final WebSocketService webSocketService;
    private final DataProcessorService dataProcessorService;

    //@Scheduled(cron = EVERY_HALF_HOUR)
    //@PostInitialize
    //public void initKlinesWS() {
    //    webSocketService.initKlinesWS();
    //}

    @Scheduled(cron = EVERY_HALF_HOUR)
    //@PostInitialize
    @PostConstruct
    public void initAggTradesWS() {
        webSocketService.initAggTradesWS();
    }

    @Scheduled(cron = EVERY_HALF_HOUR)
    //@PostInitialize
    @PostConstruct
    public void initStatisticsWS() {
        webSocketService.initStatisticsWS();
    }

    @Scheduled(cron = EVERY_HALF_HOUR)
    //@PostInitialize
    @PostConstruct
    public void initAbnormalTradingWS() {
        webSocketService.initAbnormalTradingWS();
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void clearStatisticsProcessingMap(){
        dataProcessorService.clearStatisticsProcessingMap();
    }

    /*@PostInitialize
    public void initAbnormalTradingWS() {
        webSocketService.initAbnormalTradingWS();
    }

    @PostInitialize
    public void processKlines() {
        dataProcessorService.processKlines();
    }

    @PostInitialize
    @Scheduled(fixedDelay = 1000)
    public void processAggTrades() {
        dataProcessorService.processAggTrades();
    }

    @PostInitialize
    @Scheduled(fixedDelay = 100)
    public void processStatistics() {
        dataProcessorService.processStatistics();
    }*/
}
