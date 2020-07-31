package com.jojoldu.blogcode.querydsl.domain.book;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.jojoldu.blogcode.querydsl.domain.book.QBook.book;


/**
 * Created by jojoldu@gmail.com on 30/07/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Repository
@RequiredArgsConstructor
public class BookQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public Book getBookById (Long bookId) {
        return queryFactory
                .select(book).from(book).where(book.id.eq(bookId))
                .fetchOne();
    }
}
