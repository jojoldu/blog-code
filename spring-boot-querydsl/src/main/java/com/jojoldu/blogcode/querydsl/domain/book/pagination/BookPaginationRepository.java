package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.jojoldu.blogcode.querydsl.domain.book.QBook.book;


/**
 * Created by jojoldu@gmail.com on 30/07/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RequiredArgsConstructor
@Repository
public class BookPaginationRepository {
    private final JPAQueryFactory queryFactory;
    private final JdbcTemplate jdbcTemplate;

    /**
     * 0. 기존 페이징 by Querydsl
     */
    public List<BookPaginationDto> paginationLegacy(String name, int pageNo, int pageSize) {
        return queryFactory
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
                .orderBy(book.id.desc())
                .limit(pageSize)
                .offset(pageNo * pageSize)
                .fetch();
    }

    /**
     * 1. NoOffset by Querydsl
     */
    public List<BookPaginationDto> paginationNoOffsetBuilder(Long bookId, String name, int pageSize) {

        BooleanBuilder dynamicLtId = new BooleanBuilder();

        if (bookId != null) {
            dynamicLtId.and(book.id.lt(bookId));
        }

        return queryFactory
                .select(Projections.fields(BookPaginationDto.class,
                        book.id.as("bookId"),
                        book.name,
                        book.bookNo,
                        book.bookType))
                .from(book)
                .where(dynamicLtId
                        .and(book.name.like(name + "%")))
                .orderBy(book.id.desc())
                .limit(pageSize)
                .fetch();
    }

    /**
     * 1. NoOffset by Querydsl
     */
    public List<BookPaginationDto> paginationNoOffset(Long bookId, String name, int pageSize) {

        return queryFactory
                .select(Projections.fields(BookPaginationDto.class,
                        book.id.as("bookId"),
                        book.name,
                        book.bookNo,
                        book.bookType))
                .from(book)
                .where(
                        ltBookId(bookId),
                        book.name.like(name + "%")
                )
                .orderBy(book.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression ltBookId(Long bookId) {
        if (bookId == null) {
            return null;
        }

        return book.id.lt(bookId);
    }

    /**
     * 2. 커버링 인덱스 by Querydsl
     */
    public List<BookPaginationDto> paginationCoveringIndex(String name, int pageNo, int pageSize) {
        // 1) 커버링 인덱스로 대상 조회
        List<Long> ids = queryFactory
                .select(book.id)
                .from(book)
                .where(book.name.like(name + "%"))
                .orderBy(book.id.desc())
                .limit(pageSize)
                .offset(pageNo * pageSize)
                .fetch();

        // 1-1) 대상이 없을 경우 추가 쿼리 수행 할 필요 없이 바로 반환
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        // 2)
        return queryFactory
                .select(Projections.fields(BookPaginationDto.class,
                        book.id.as("bookId"),
                        book.name,
                        book.bookNo,
                        book.bookType))
                .from(book)
                .where(book.id.in(ids))
                .fetch().stream()
                .sorted() // where in id만 있어 결과 정렬이 보장되지 않는다. (쿼리 시간 최적화를 위해 애플리케이션에서 정렬한다)
                .collect(Collectors.toList());
    }

    /**
     * 2. 커버링 인덱스 by JdbcTemplate
     */
    public List<BookPaginationDto> paginationCoveringIndexSql(String name, int pageNo, int pageSize) {
        String query =
                "SELECT i.id, book_no, book_type, name " +
                "FROM book as i " +
                "JOIN (SELECT id " +
                "       FROM book " +
                "       WHERE name LIKE '?%' " +
                "       ORDER BY id DESC " +
                "       LIMIT ? " +
                "       OFFSET ?) as temp on temp.id = i.id";

        return jdbcTemplate
                .query(query, new BeanPropertyRowMapper<>(BookPaginationDto.class),
                        name,
                        pageSize,
                        pageNo * pageSize);
    }

}
