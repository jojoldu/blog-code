package com.blogcode.example2;

import java.util.function.Function;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 7.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public enum SalesAmountType {
    ORIGIN_AMOUNT("원금액", amount -> amount),
    SUPPLY_AMOUNT("공급가액", amount -> Math.round(amount.doubleValue() * 10 / 11)),
    VAT_AMOUNT("부가세", amount -> Math.round(amount.doubleValue() / 11)),
    NOT_USED("사용안함", amount -> 0L);

    private String text;
    private Function<Long, Long> expression;

    SalesAmountType(String text, Function<Long, Long> expression) {
        this.text = text;
        this.expression = expression;
    }

    public long calculate(long amount){
        return expression.apply(amount);
    }

    public String getText() {
        return text;
    }
}
