package com.dodonov.detector.service.impl;

import com.dodonov.detector.repository.DataProcessorRepository;
import com.dodonov.detector.repository.PumpCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.HOURS;

//@Service
@RequiredArgsConstructor
public class DataProcessorServiceImplDeprecated
        //implements DataProcessorService
{
    private final DataProcessorRepository dataProcessorRepository;
    private final PumpCheckRepository pumpCheckRepository;
    private final Object aggTradesLock = new Object();
    private final Object statisticsLock = new Object();
    private volatile LocalDateTime startTimeFrame10Minutes;
    private final LocalDateTime startTimeFrame1Hour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).minus(1, HOURS);

    //@Override
    @Async
    public void processAggTrades() {
    /*    synchronized (aggTradesLock) {
            if (checkStartTimeFrame1Hour()) {
                var result = dataProcessorRepository.processAggTrades(startTimeFrame1Hour, LocalDateTime.now());
                if (!result.isEmpty()) {
                    dataProcessorRepository.batchInsertAggTradeProcessView(result, startTimeFrame1Hour);
                }
                incrementStartTimeFrame1Hour();
            }
        }*/
    }

    //@Override
    @Async
    public void processStatistics() {
        /*synchronized (statisticsLock) {
            setStartTimeFrame10Minutes();
            var result = dataProcessorRepository.processStatistics(startTimeFrame10Minutes, LocalDateTime.now().minus(10, SECONDS));
            if (!result.isEmpty()) {
                var alreadyContaining = new HashSet<>(dataProcessorRepository.checkBeforeInsert(result.stream()
                        .map(StatisticsProcessView::getSymbol)
                        .collect(Collectors.toList()), startTimeFrame10Minutes));
                var toInsert = result.stream()
                        .filter(s -> !alreadyContaining.contains(s.getSymbol()))
                        .collect(Collectors.toSet());
                dataProcessorRepository.batchInsertStatisticsProcessView(toInsert);
                Set<PumpCheck> pumpChecks = dataProcessorRepository.checkPumps(toInsert);
                pumpCheckRepository.saveAll(pumpChecks);
            }
        }
    }

    private synchronized void setStartTimeFrame10Minutes() {
        if (startTimeFrame10Minutes == null) {
            startTimeFrame10Minutes = LocalDateTime.now().withSecond(0).withNano(0).minusMinutes(5);
            return;
        }
        LocalDateTime temp = LocalDateTime.from(startTimeFrame10Minutes);
        LocalDateTime now = LocalDateTime.now();
        long minutes = temp.until(now, MINUTES);
        if (minutes >= 10) {
            startTimeFrame10Minutes = startTimeFrame10Minutes.plus(1, MINUTES);
        }
    }

    private synchronized boolean checkStartTimeFrame1Hour() {
        return (startTimeFrame1Hour.getHour() < LocalDateTime.now().getHour()) || (startTimeFrame1Hour.getHour() == 23);
    }

    private synchronized LocalDateTime incrementStartTimeFrame1Hour() {
        return LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);*/
    }

}
