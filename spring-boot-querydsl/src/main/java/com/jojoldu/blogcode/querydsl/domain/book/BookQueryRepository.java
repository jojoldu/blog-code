package com.jojoldu.blogcode.querydsl.domain.book;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.jojoldu.blogcode.querydsl.domain.book.QBook.book;


/**
 * Created by jojoldu@gmail.com on 30/07/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RequiredArgsConstructor
@Repository
public class BookQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Book getBookById (Long bookId) {
        return queryFactory
                .select(book)
                .from(book)
                .where(book.id.eq(bookId))
                .fetchOne();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public String getBookNameById (Long bookId) {
        return queryFactory
                .select(book.name)
                .from(book)
                .where(book.id.eq(bookId))
                .fetchOne();
    }

    @Transactional(readOnly = true)
    public Boolean exist(Long bookId) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(book)
                .where(book.id.eq(bookId))
                .fetchFirst();

        return fetchOne > 0;
    }
}
