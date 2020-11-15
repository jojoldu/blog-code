package com.jojoldu.blogcode.querydsl.domain.ad;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jojoldu.blogcode.querydsl.domain.ad.QCustomer.customer;
import static com.jojoldu.blogcode.querydsl.domain.ad.QShop.shop;

/**
 * Created by jojoldu@gmail.com on 15/11/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@RequiredArgsConstructor
@Repository
public class CustomerQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Customer> crossJoin() {
        return queryFactory
                .selectFrom(customer)
                .where(customer.customerNo.gt(customer.shop.shopNo))
                .fetch();
    }

    public List<Customer> notCrossJoin() {
        return queryFactory
                .selectFrom(customer)
                .innerJoin(customer.shop, shop)
                .where(customer.customerNo.gt(shop.shopNo))
                .fetch();
    }
}
