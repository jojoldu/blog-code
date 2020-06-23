package com.jojoldu.blogcode.querydsl.domain.pointevent;

import com.jojoldu.blogcode.querydsl.dto.PointCalculateAmount;
import com.jojoldu.blogcode.querydsl.config.OrderByNull;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.jojoldu.blogcode.querydsl.domain.pointevent.QPointEvent.pointEvent;

/**
 * Created by jojoldu@gmail.com on 2018-12-29
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RequiredArgsConstructor
public class PointEventRepositoryImpl implements PointEventRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PointCalculateAmount> calculateAmounts() {
        return queryFactory
                .select(Projections.fields(PointCalculateAmount.class,
                        new CaseBuilder()
                                .when(pointEvent.pointStatus.in(PointStatus.USE, PointStatus.USE_CANCEL))
                                .then(pointEvent.pointAmount.multiply(-1))
                                .otherwise(pointEvent.pointAmount).as("pointAmount"),
                        pointEvent.pointStatus
                ))
                .from(pointEvent)
                .fetch();
    }

    @Override
    public List<Integer> getGroupOne() {
        return queryFactory.select(Expressions.ONE)
                .from(pointEvent)
                .groupBy(pointEvent.pointStatus)
                .orderBy(OrderByNull.DEFAULT)
                .fetch();
    }
}
