package com.jojoldu.spock.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public enum FeeCalculateType {

    DECIMAL_FIRST_HALF_UP("소수점 첫째자리 반올림", (amount) ->
            BigDecimal.valueOf(amount)
                    .setScale(0, RoundingMode.HALF_UP)
                    .longValue()),
    
    DECIMAL_FIRST_UP("소수점 첫째자리 올림", (amount) ->
            BigDecimal.valueOf(amount)
                    .setScale(0, RoundingMode.UP)
                    .longValue()),
    
    DECIMAL_FIRST_DOWN("소수점 첫째자리 버림", (amount) ->
            BigDecimal.valueOf(amount)
                    .setScale(0, RoundingMode.DOWN)
                    .longValue()),
    
    WON_UNIT_CUT("원단위 절사", (amount) ->
            BigDecimal.valueOf(amount)
                    .setScale(-1, RoundingMode.DOWN)
                    .longValue());

    FeeCalculateType(String text, Function<Long, Long> expression) {
        this.text = text;
        this.expression = expression;
    }

    private String text;
    private Function<Long, Long> expression;

    public long calculate(long amount) { return expression.apply(amount); }

    public String getId() {
        return name();
    }

    public String getText() {
        return text;
    }
}