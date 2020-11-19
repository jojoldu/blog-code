package com.jojoldu.blogcode.querydsl.domain.ad;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.jojoldu.blogcode.querydsl.domain.ad.QAdItem.adItem;
import static com.querydsl.core.types.dsl.Expressions.constantAs;

/**
 * Created by jojoldu@gmail.com on 11/08/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RequiredArgsConstructor
@Repository
public class AdItemQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public List<AdBond> createAdBond(LocalDate startDate, LocalDate endDate, List<String> orderTypes) {
        if(CollectionUtils.isEmpty(orderTypes)) {
            return new ArrayList<>();
        }

        return queryFactory
                .select(Projections.fields(AdBond.class,
                        adItem.amount.sum().as("amount"),
                        adItem.txDate,
                        adItem.orderType,
                        adItem.customer)
                )
                .from(adItem)
                .where(adItem.orderType.in(orderTypes)
                        .and(adItem.txDate.between(startDate, endDate)))
                .groupBy(adItem.orderType, adItem.txDate, adItem.customer)
                .fetch();
    }

    @Transactional(readOnly = true)
    public List<AdBondDto> createAdBondDto(LocalDate startDate, LocalDate endDate, List<String> orderTypes) {
        if(CollectionUtils.isEmpty(orderTypes)) {
            return new ArrayList<>();
        }

        return queryFactory
                .select(Projections.fields(AdBondDto.class,
                        adItem.amount.sum().as("amount"),
                        adItem.txDate,
                        adItem.orderType,
                        adItem.customer.id.as("customerId"))
                )
                .from(adItem)
                .where(adItem.orderType.in(orderTypes)
                        .and(adItem.txDate.between(startDate, endDate)))
                .groupBy(adItem.orderType, adItem.txDate, adItem.customer)
                .fetch();
    }
}
