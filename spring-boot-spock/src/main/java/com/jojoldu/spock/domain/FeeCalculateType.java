package com.jojoldu.spock.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public enum FeeCalculateType {

    DECIMAL_FIRST_HALF_UP("소수점 첫째자리 반올림", (amount) -> {
        validAmount(amount);
        return calculate(amount, 0, RoundingMode.HALF_UP);
    }),
    
    DECIMAL_FIRST_UP("소수점 첫째자리 올림", (amount) -> {
        validAmount(amount);
        return calculate(amount, 0, RoundingMode.UP);
    }),
    
    DECIMAL_FIRST_DOWN("소수점 첫째자리 버림", (amount) -> {
        validAmount(amount);
        return calculate(amount, 0, RoundingMode.DOWN);
    }),
    
    WON_UNIT_CUT("원단위 버림", (amount) -> {
        validAmount(amount);
        return calculate(amount, -1, RoundingMode.DOWN);
    });

    private String title;
    private Function<Long, Long> expression;

    FeeCalculateType(String title, Function<Long, Long> expression) {
        this.title = title;
        this.expression = expression;
    }

    private static void validAmount(long amount){
        if(amount < 0){
            throw new NegativeNumberException("음수는 허용하지 않습니다.");
        }
    }

    private static long calculate(long amount, int scale, RoundingMode mode){
        return BigDecimal.valueOf(amount)
                .setScale(scale,mode)
                .longValue();
    }

    public long calculate(long amount) { return expression.apply(amount); }

}