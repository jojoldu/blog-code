package com.jojoldu.blogcode.querydsl.domain.order;

/**
 * Created by jojoldu@gmail.com on 22/11/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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

    @Convert(converter = PaysConverter.class)
    private List<Pay> pays = new ArrayList<>();

    public Order(String orderNo, List<Pay> pays) {
        this.orderNo = orderNo;
        this.pays = pays;
    }
}
