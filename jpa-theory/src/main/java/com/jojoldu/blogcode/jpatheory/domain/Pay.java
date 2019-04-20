package com.jojoldu.blogcode.jpatheory.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tradeNo;
    private long amount;

    @OneToMany(mappedBy = "pay", cascade = CascadeType.ALL)
    private List<PayDetail> details = new ArrayList<>();

    public Pay(String tradeNo, long amount) {
        this.tradeNo = tradeNo;
        this.amount = amount;
    }

    public void addDetail(PayDetail detail) {
        this.details.add(detail);
        detail.setPay(this);
    }

    public void changeTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
}
