package com.blogcode.example3;

import java.util.Arrays;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 8.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public enum PaymentGroup {

    CASH("현금", new PaymentOption[]{
        PaymentOption.BANK_TRANSFER, PaymentOption.DEPOSITLESS, PaymentOption.FIELD_PAYMENT, PaymentOption.TOSS
    }),
    PG("결제대행사", new PaymentOption[]{
        PaymentOption.MOBILE, PaymentOption.CREDIT_CARD, PaymentOption.SIMPLE_PAY
    }),
    ETC("기타", new PaymentOption[]{
        PaymentOption.POINT, PaymentOption.COUPON
    }),
    EMPTY("없음", new PaymentOption[]{});

    private String viewName;
    private PaymentOption[] containPayment;

    PaymentGroup(String viewName, PaymentOption[] containPayment) {
        this.viewName = viewName;
        this.containPayment = containPayment;
    }

    public static PaymentGroup findGroup(PaymentOption searchTarget){
        return Arrays.stream(PaymentGroup.values())
                .filter(group -> hasPaymentOption(group, searchTarget))
                .findAny()
                .orElse(PaymentGroup.EMPTY);
    }

    private static boolean hasPaymentOption(PaymentGroup from, PaymentOption searchTarget){
        return Arrays.stream(from.containPayment)
                .anyMatch(containPay -> containPay == searchTarget);
    }

    public String getViewName() {
        return viewName;
    }
}
