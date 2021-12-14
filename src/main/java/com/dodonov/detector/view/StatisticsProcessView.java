package com.dodonov.detector.view;

import lombok.Data;
import org.hibernate.transform.ResultTransformer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
public class StatisticsProcessView {
    private String symbol;
    private BigDecimal change;
    private Long startId;
    private Long lastId;
    private LocalDateTime startTimeFrame;

    public StatisticsProcessView(BigDecimal change, String symbol, Long startId, Long lastId, LocalDateTime startTimeFrame) {
        this.symbol = symbol;
        this.change = change;
        this.startId = startId;
        this.lastId = lastId;
        this.startTimeFrame = startTimeFrame;
    }

    public static ResultTransformer transformer() {
        return new StatisticsProcessViewResultTransformer();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticsProcessView that = (StatisticsProcessView) o;
        return Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    private static class StatisticsProcessViewResultTransformer implements ResultTransformer {

        @Override
        public Object transformTuple(Object[] objects, String[] strings) {
            return new StatisticsProcessView(
                    ((BigDecimal) objects[0]),
                    ((String) objects[1]),
                    ((BigInteger) objects[2]).longValue(),
                    ((BigInteger) objects[3]).longValue(),
                    ((LocalDateTime) LocalDateTime.parse(objects[4].toString().substring(0, 19).replace(" ", "T")))
            );
        }

        @Override
        public List transformList(List list) {
            return list;
        }
    }
}
