package com.jojoldu.blogcode.querydsl.domain.order;

/**
 * Created by jojoldu@gmail.com on 22/11/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNo;

    @Embedded
    private Member member;

    @Convert(converter = PeriodStringConverter.class)
    private Period period;

    @Convert(converter = PayConverter.class)
    private List<Pay> pays = new ArrayList<>();

    public Order(String orderNo, Period period, Member member, Pay pay) {
        this.orderNo = orderNo;
        this.period = period;
        this.member = member;
        this.pays.add(pay);
    }
}
