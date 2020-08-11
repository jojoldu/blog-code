package com.jojoldu.blogcode.querydsl.domain.ad;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerNo;  //거래처번호(업주번호)
    private String customerName; //거래처명(상호)
    private String bizNo; //사업자번호
    private String ceoName; //대표자명

    @OneToOne(mappedBy = "customer")
    private Shop shop;

    public Customer(Long id) {
        this.id = id;
    }

    public Customer(String customerNo, String customerName, String bizNo, String ceoName) {
        this.customerNo = customerNo;
        this.customerName = customerName;
        this.bizNo = bizNo;
        this.ceoName = ceoName;
    }

    @Builder
    public Customer(String customerNo, String customerName, String bizNo, String ceoName, Shop shop) {
        this.customerNo = customerNo;
        this.customerName = customerName;
        this.bizNo = bizNo;
        this.ceoName = ceoName;
        setShop(shop);
    }

    public void setShop(Shop shop) {
        this.shop = shop;
        shop.setCustomer(this);
    }
}
