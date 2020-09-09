package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import com.jojoldu.blogcode.querydsl.domain.book.BookType;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by jojoldu@gmail.com on 02/09/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Getter
@NoArgsConstructor
public class BookPaginationDto {
    private Long bookId;
    private String name;
    private int bookNo;
    private BookType bookType;
}
