package com.jojoldu.blogcode.querydsl.domain.book;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by jojoldu@gmail.com on 02/09/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Getter
@NoArgsConstructor
public class BookPageDto {
    private String name;
    private int pageNo;
    private int bookNo;
}
