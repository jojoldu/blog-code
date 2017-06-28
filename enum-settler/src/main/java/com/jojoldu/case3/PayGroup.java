package com.jojoldu.case3;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 28.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public enum PayGroup {

    CASH("현금", Arrays.asList("ACCOUNT_TRANSFER", "REMITTANCE", "ON_SITE_PAYMENT", "TOSS")),
    CARD("카드", Arrays.asList("PAYCO", "CARD", "KAKAO_PAY", "BAEMIN_PAY")),
    ETC("기타", Arrays.asList("POINT", "COUPON")),
    EMPTY("없음", Collections.singletonList(""));

    private String title;
    private List<String> payList;

    PayGroup(String title, List<String> payList) {
        this.title = title;
        this.payList = payList;
    }

    public static PayGroup findByPayCode(String code){
        return Arrays.stream(PayGroup.values())
                .filter(payGroup -> payGroup.hasPayCode(code))
                .findAny()
                .orElse(EMPTY);
    }

    public boolean hasPayCode(String code){
        return payList.stream()
                .anyMatch(pay -> pay.equals(code));
    }

    public String getTitle() {
        return title;
    }

    public List<String> getPayList() {
        return payList;
    }
}
