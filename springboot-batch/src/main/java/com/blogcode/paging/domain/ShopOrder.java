package com.blogcode.paging.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

/**
 * Created by jojoldu@gmail.com on 2017. 7. 26.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Entity
@Getter
@NoArgsConstructor
public class ShopOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate orderDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "FK_ORDER_CUSTOMER"))
    private Customer customer;

    public ShopOrder(LocalDate orderDate, Customer customer) {
        this.orderDate = orderDate;
        this.customer = customer;
    }

    public void updateCustomer(Customer customer){
        this.customer = customer;
    }
}
