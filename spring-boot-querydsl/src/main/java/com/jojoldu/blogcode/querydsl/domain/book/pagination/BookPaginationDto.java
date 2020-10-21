package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import com.jojoldu.blogcode.querydsl.domain.book.BookType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by jojoldu@gmail.com on 02/09/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Setter
@Getter
@NoArgsConstructor
public class BookPaginationDto implements Comparable<BookPaginationDto>{
    private Long bookId;
    private String name;
    private int bookNo;
    private BookType bookType;

    public BookPaginationDto(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public int compareTo(BookPaginationDto o) {
        return o.bookId.compareTo(this.bookId);
    }
}
