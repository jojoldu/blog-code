package com.jojoldu.case3;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 28.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class LegacyPayGroup {

    public static String getPayGroup(String payCode){

        if("ACCOUNT_TRANSFER".equals(payCode) || "REMITTANCE".equals(payCode) || "ON_SITE_PAYMENT".equals(payCode) || "TOSS".equals(payCode)){
            return "CASH";

        } else if("PAYCO".equals(payCode) || "CARD".equals(payCode) || "KAKAO_PAY".equals(payCode) || "BAEMIN_PAY".equals(payCode)){
            return "CARD";

        } else if("POINT".equals(payCode) || "COUPON".equals(payCode)){
            return "ETC";

        } else {
            return "EMPTY";
        }
    }
}
