package com.jojoldu.blogcode.querydsl.domain.book.store;

/**
 * Created by jojoldu@gmail.com on 2019-01-11
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "book_store",
        indexes = @Index(name = "idx_book_store_1", columnList = "name") // id는 클러스터인덱스라 인덱스에 자동 포함
)
public class BookStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "name")
    private String name;

    @Embedded
    private BookStoreLocation storeLocation;

    @Builder
    public BookStore(String name, String location, String zipCode) {
        this.name = name;
        this.storeLocation = BookStoreLocation.builder()
                .location(location)
                .zipCode(zipCode)
                .build();
    }
}
