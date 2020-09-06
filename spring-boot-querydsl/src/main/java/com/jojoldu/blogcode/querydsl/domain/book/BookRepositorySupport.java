package com.jojoldu.blogcode.querydsl.domain.book;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.jojoldu.blogcode.querydsl.domain.book.QBook.book;

/**
 * Created by jojoldu@gmail.com on 06/09/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Repository
public class BookRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public BookRepositorySupport(JPAQueryFactory queryFactory) {
        super(Book.class);
        this.queryFactory = queryFactory;
    }

    public Page<Book> getBooks(Pageable pageable, BookType bookType) {
        JPAQuery<Book> query = queryFactory
                .selectFrom(book)
                .where(book.bookType.eq(bookType))
                .orderBy(book.id.desc());

        JPQLQuery<Book> paginationQuery = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);
        List<Book> elements = paginationQuery.fetch();
        long totalCount = paginationQuery.fetchCount();
        return new PageImpl<>(elements, pageable, totalCount);
    }

}
