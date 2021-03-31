package com.jojoldu.blogcode.springbootjpa.domain.pay;

/**
 * Created by jojoldu@gmail.com on 30/03/2021
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long amount;
    private String orderNo;

    @Embedded
    private PayDetails payDetails = PayDetails.EMPTY;

    @Embedded
    private PayEvents payEvents = new PayEvents();

    @Builder
    public Pay(long amount, String orderNo) {
        this.amount = amount;
        this.orderNo = orderNo;
    }

    public void addPayDetail(PayDetail payDetail) {
        this.payDetails.add(this, payDetail);
    }

    public void addPayEvent(PayEvent payEvent) {
        this.payEvents.add(this, payEvent);
    }

    public PayDetail getPayDetail(int index) {
        return payDetails.get(index);
    }

    public PayEvent getPayEvent(int index) {
        return payEvents.get(index);
    }

}
