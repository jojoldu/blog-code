package com.jojoldu.blogcode.querydsl.domain.book.store;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by jojoldu@gmail.com on 19/11/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Getter
@NoArgsConstructor
@Embeddable
public class BookStoreLocation {

    @Column(name = "location")
    private String location;

    @Column(name = "zip_code")
    private String zipCode;

    @Builder
    public BookStoreLocation(String location, String zipCode) {
        this.location = location;
        this.zipCode = zipCode;
    }
}
