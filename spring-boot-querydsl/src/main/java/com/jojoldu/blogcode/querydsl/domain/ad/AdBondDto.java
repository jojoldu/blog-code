package com.jojoldu.blogcode.querydsl.domain.ad;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AdBondDto {

    private long amount;
    private LocalDate txDate;
    private String orderType;
    private Long customerId;

    public AdBond toEntity(Customer customer) {
        return AdBond.builder()
                .amount(amount)
                .txDate(txDate)
                .orderType(orderType)
                .customer(customer)
                .build();
    }

    public AdBond toEntity() {
        return AdBond.builder()
                .amount(amount)
                .txDate(txDate)
                .orderType(orderType)
                .customer(new Customer(customerId))
                .build();
    }
}
