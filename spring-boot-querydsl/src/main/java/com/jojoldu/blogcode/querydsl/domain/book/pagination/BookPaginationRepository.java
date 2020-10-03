package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import com.jojoldu.blogcode.querydsl.domain.book.BookType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jojoldu.blogcode.querydsl.domain.book.QBook.book;
import static com.querydsl.core.types.dsl.Expressions.constantAs;


/**
 * Created by jojoldu@gmail.com on 30/07/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RequiredArgsConstructor
@Repository
public class BookPaginationRepository {
    private final JPAQueryFactory queryFactory;

    public List<BookPaginationDto> paginationLegacy (BookType bookType, String name, int pageNo) {
        return queryFactory
                .select(Projections.fields(BookPaginationDto.class,
                        book.id.as("bookId"),
                        book.name,
                        book.bookNo,
                        constantAs(bookType, book.bookType)))
                .from(book)
                .where(
                        book.bookType.eq(bookType),
                        book.name.like("%"+name+"%")
                )
                .limit(10)
                .offset(pageNo)
                .orderBy(book.id.desc())
                .fetch();
    }

    public List<BookPaginationDto> paginationNoOffset (Long bookId, BookType bookType, String name) {
        return queryFactory
                .select(Projections.fields(BookPaginationDto.class,
                        book.id.as("bookId"),
                        book.name,
                        book.bookNo,
                        constantAs(bookType, book.bookType)))
                .from(book)
                .where(
                        ltBookId(bookId),
                        book.bookType.eq(bookType),
                        book.name.like("%"+name+"%")
                )
                .limit(10)
                .orderBy(book.id.desc())
                .fetch();
    }

    private BooleanExpression ltBookId (Long bookId) {
        if(bookId == null) {
            return null;
        }

        return book.id.lt(bookId);
    }

}
