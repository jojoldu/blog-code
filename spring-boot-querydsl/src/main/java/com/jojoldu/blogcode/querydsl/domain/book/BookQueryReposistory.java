package com.jojoldu.blogcode.querydsl.domain.book;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by jojoldu@gmail.com on 30/07/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Repository
@RequiredArgsConstructor
public class BookQueryReposistory {
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public Book getBookById (Long bookId) {
        return queryFactory
                .select(book).from(book).where(book.id)
    }
}
