package com.jojoldu.blogcode.jpatheory.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@Entity
public class PayDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private long amount;

    @ManyToOne
    @JoinColumn(name = "pay_id", foreignKey =  @ForeignKey(name ="fk_pay_detail_pay_id"))
    private Pay pay;

    public PayDetail(String type, long amount) {
        this.type = type;
        this.amount = amount;
    }

    public void setPay(Pay pay) {
        this.pay = pay;
    }
}
