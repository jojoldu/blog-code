package com.jojoldu.blogcode.firstclasscollection.sample1;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayType {
    NAVER_PAY,
    KAKAO_PAY,
    TOSS,
    PAYCO;

    public static boolean isNaverPay (PayType target) {
        return NAVER_PAY.equals(target);
    }

    public static boolean isKakaoPay(PayType target) {
        return NAVER_PAY.equals(target);
    }
}
