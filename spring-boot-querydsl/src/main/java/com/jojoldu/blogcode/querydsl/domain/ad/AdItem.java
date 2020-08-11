package com.jojoldu.blogcode.querydsl.domain.ad;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;


@NoArgsConstructor
@Getter
@Entity
@Table(indexes = {
        @Index(name = "idx_ad_item_1", columnList = "orderType, txDate")
})
public class AdItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderType; // 주문유형
    private LocalDate txDate; //거래일자
    private String serviceTxNo; //서비스 거래 번호
    private long amount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "fk_ad_item_customer"))
    private Customer customer;

    @Builder
    public AdItem(String orderType, LocalDate txDate, String serviceTxNo, long amount, Customer customer) {
        this.orderType = orderType;
        this.txDate = txDate;
        this.serviceTxNo = serviceTxNo;
        this.amount = amount;
        setCustomer(customer);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
