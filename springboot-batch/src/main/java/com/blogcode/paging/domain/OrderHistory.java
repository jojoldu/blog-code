package com.blogcode.paging.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by jojoldu@gmail.com on 2017. 7. 26.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Entity
@Getter
@NoArgsConstructor
public class OrderHistory {

    @Id @GeneratedValue
    private Long id;

    private long orderId;
    private String customerName;

    public OrderHistory(long orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
    }
}
