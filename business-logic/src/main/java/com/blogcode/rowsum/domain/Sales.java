package com.blogcode.rowsum.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 22.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Entity
@NoArgsConstructor
@Getter
public class Sales {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long ownerId;

    @Column
    private LocalDate payDate;

    @Column
    private String calculateCode;

    @Column
    private int totalAmount;

    @Column
    private int mobileAmount;

    @Column
    private int creditCardAmount;

    @Column
    private int cashAmount;

    @Builder
    public Sales(Long ownerId, LocalDate payDate, String calculateCode, int totalAmount, int mobileAmount, int creditCardAmount, int cashAmount) {
        this.ownerId = ownerId;
        this.payDate = payDate;
        this.calculateCode = calculateCode;
        this.totalAmount = totalAmount;
        this.mobileAmount = mobileAmount;
        this.creditCardAmount = creditCardAmount;
        this.cashAmount = cashAmount;
    }

    public void add(int amount, Payment.Method paymentMethod){
        if(paymentMethod == Payment.Method.MOBILE){ mobileAmount = amount; }
        else if(paymentMethod == Payment.Method.CREDIT_CARD){ creditCardAmount = amount; }
        else if(paymentMethod == Payment.Method.CASH) { cashAmount = amount; }
        totalAmount += amount;
    }
}
