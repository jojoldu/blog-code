package com.jojoldu.blogcode.firstclasscollection.sample4;


import lombok.Builder;
import lombok.Getter;

@Getter
public class Pay {

    private PayType payType;
    private long amount;

    @Builder
    public Pay(PayType payType, long amount) {
        this.payType = payType;
        this.amount = amount;
    }

}
