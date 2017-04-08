package com.blogcode.example3;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 8.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public enum PaymentOption {
    MOBILE("모바일"),
    CREDIT_CARD("신용카드"),
    SIMPLE_PAY("간편결제"),
    BANK_TRANSFER("계좌이체"),
    DEPOSITLESS("무통장입금"),
    FIELD_PAYMENT("현장결제"),
    TOSS("토스"),
    POINT("포인트"),
    COUPON("쿠폰");

    private String viewName;

    PaymentOption(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }
}
