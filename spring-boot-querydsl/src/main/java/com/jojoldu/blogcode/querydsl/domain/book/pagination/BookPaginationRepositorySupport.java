package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import com.jojoldu.blogcode.querydsl.domain.book.Book;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
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
        JPQLQuery<BookPaginationDto> query = querydsl().applyPagination(pageable,
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

        List<BookPaginationDto> items = query.fetch(); // 데이터 조회
        long totalCount = query.fetchCount(); // 전체 count
        return new PageImpl<>(items, pageable, totalCount);
    }

    /**
     * 3-1. 검색 버튼사용 count 고정값 쓰기
     */
    public Page<BookPaginationDto> paginationCountSearchBtn(boolean useSearchBtn, Pageable pageable, String name) {
        JPAQuery<BookPaginationDto> query = queryFactory
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
                .orderBy(book.id.desc());

        JPQLQuery<BookPaginationDto> pagination = querydsl().applyPagination(pageable, query);

        if(useSearchBtn) {
            int fixedPageCount = 10 * pageable.getPageSize(); // 10개 페이지 고정
            return new PageImpl<>(pagination.fetch(), pageable, fixedPageCount);
        }

        long totalCount = pagination.fetchCount();
        Pageable pageRequest = exchangePageRequest(pageable, totalCount);
        return new PageImpl<>(querydsl().applyPagination(pageRequest, query).fetch(), pageRequest, totalCount);
    }

    Pageable exchangePageRequest(Pageable pageable, long totalCount) {

        /**
         *  요청한 페이지 번호가 기존 데이터 사이즈를 초과할 경우
         *  마지막 페이지의 데이터를 반환한다
         */
        int pageNo = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long requestCount = (pageNo - 1) * pageSize; // pageNo:10, pageSize:10 일 경우 requestCount=90

        if (totalCount > requestCount) { //
            return pageable;
        }

        int requestPageNo = (int) totalCount / pageSize;
        return PageRequest.of(requestPageNo, pageSize);

    }

    public Page<BookPaginationDto> paginationCountSearchBtn2(boolean useSearchBtn, Pageable pageable, String name) {
        JPAQuery<BookPaginationDto> query = queryFactory
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
                .orderBy(book.id.desc());

        JPQLQuery<BookPaginationDto> pagination = querydsl().applyPagination(pageable, query);

        if(useSearchBtn) {
            int fixedPageCount = 10 * pageable.getPageSize(); // 10개 페이지 고정
            return new PageImpl<>(pagination.fetch(), pageable, fixedPageCount);
        }

        long totalCount = pagination.fetchCount();
        Pageable pageRequest = new FixedPageRequest(pageable, totalCount);
        return new PageImpl<>(querydsl().applyPagination(pageRequest, query).fetch(), pageRequest, totalCount);
    }

    Querydsl querydsl() {
        return Objects.requireNonNull(getQuerydsl());
    }


    /**
     * 3-2. 첫 페이지 조회 결과 cache 하기
     */
    public Page<BookPaginationDto> paginationCountCache(Long cachedCount, Pageable pageable, String name) {
        JPQLQuery<BookPaginationDto> query = querydsl().applyPagination(pageable,
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

        List<BookPaginationDto> elements = query.fetch(); // 데이터 조회
        long totalCount = cachedCount != null ? cachedCount : query.fetchCount(); // 전체 count
        return new PageImpl<>(elements, pageable, totalCount);
    }

}
