package com.jojoldu.blogcode.querydsl.domain.ad;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
@NoArgsConstructor
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shopNo;
    private String shopName;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Shop(String shopNo, String shopName) {
        this.shopNo = shopNo;
        this.shopName = shopName;
    }

    public Shop(String shopNo, String shopName, Customer customer) {
        this.shopNo = shopNo;
        this.shopName = shopName;
        setCustomer(customer);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
