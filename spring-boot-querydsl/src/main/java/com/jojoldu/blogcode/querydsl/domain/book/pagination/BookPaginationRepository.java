package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import com.jojoldu.blogcode.querydsl.domain.book.BookType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<BookPaginationDto> paginationLegacy (String name, int pageNo) {
        return queryFactory
                .select(Projections.fields(BookPaginationDto.class,
                        book.id.as("bookId"),
                        book.name,
                        book.bookNo))
                .from(book)
                .where(
                        book.name.like("%" + name + "%")
                )
                .orderBy(book.id.desc())
                .limit(10)
                .offset(pageNo)
                .fetch();
    }

    public List<BookPaginationDto> paginationNoOffset (Long bookId, String name) {
        return queryFactory
                .select(Projections.fields(BookPaginationDto.class,
                        book.id.as("bookId"),
                        book.name,
                        book.bookNo))
                .from(book)
                .where(
                        ltBookId(bookId),
                        book.name.like("%" + name + "%")
                )
                .orderBy(book.id.desc())
                .limit(10)
                .fetch();
    }

    public List<BookPaginationDto> paginationCoveringIndex (String name, int pageNo) {
        return queryFactory
                .select(Projections.fields(BookPaginationDto.class,
                        book.id.as("bookId"),
                        book.name,
                        book.bookNo))
                .from(book)
                .where(
                        book.name.like("%" + name + "%")
                )
                .orderBy(book.id.desc())
                .limit(10)
                .offset(pageNo)
                .fetch();
    }

    private BooleanExpression ltBookId (Long bookId) {
        if(bookId == null) {
            return null;
        }

        return book.id.lt(bookId);
    }

}
