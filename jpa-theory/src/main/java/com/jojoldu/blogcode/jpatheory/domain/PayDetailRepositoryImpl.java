package com.jojoldu.blogcode.jpatheory.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PayDetailRepositoryImpl implements PayDetailRepositoryCustom{
    private final JPAQueryFactory queryFactory;


}
