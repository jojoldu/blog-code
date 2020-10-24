package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import com.jojoldu.blogcode.querydsl.domain.book.Book;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Repository
public class BookPaginationRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public BookPaginationRepositorySupport(JPAQueryFactory queryFactory) {
        super(Book.class);
        this.queryFactory = queryFactory;
    }

    public Page<BookPaginationDto> paginationCount(Pageable pageable, String name) {
        JPQLQuery<BookPaginationDto> query = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable,
                queryFactory
                .select(Projections.fields(BookPaginationDto.class,
                        book.id.as("bookId"),
                        book.name,
                        book.bookNo,
                        book.bookType
                ))
                .from(book)
                .where(
                        book.name.like(name + "%")
                )
                .orderBy(book.id.desc()));

        log.info(">>>>>>> query.fetch()");
        List<BookPaginationDto> elements = query.fetch(); // 데이터 조회
        log.info(">>>>>>> query.fetchCount()");
        long totalCount = query.fetchCount(); // 전체 count
        return new PageImpl<>(elements, pageable, totalCount);
    }

}
