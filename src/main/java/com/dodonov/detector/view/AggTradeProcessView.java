package com.dodonov.detector.view;

import lombok.Data;
import org.hibernate.transform.ResultTransformer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
public class AggTradeProcessView {
    private String symbol;
    private LocalDateTime eventTime;
    private LocalDateTime timeframe;
    private BigDecimal buyQuantity;
    private BigDecimal sellQuantity;
    private BigDecimal ratio;

    public AggTradeProcessView(String symbol, BigDecimal buyQuantity, BigDecimal sellQuantity, BigDecimal ratio) {
        this.symbol = symbol;
        this.buyQuantity = buyQuantity;
        this.sellQuantity = sellQuantity;
        this.ratio = ratio;
    }

    public static ResultTransformer transformer() {
        return new AggTradeProcessViewResultTransformer();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggTradeProcessView that = (AggTradeProcessView) o;
        return Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    private static class AggTradeProcessViewResultTransformer implements ResultTransformer {

        @Override
        public Object transformTuple(Object[] objects, String[] strings) {
            return new AggTradeProcessView(
                    (String) objects[0],
                    (BigDecimal) objects[1],
                    (BigDecimal) objects[2],
                    (BigDecimal) objects[3]
            );
        }

        @Override
        public List transformList(List list) {
            return list;
        }
    }
}
