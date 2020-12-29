package com.jojoldu.blogcode.querydsl.domain.book;

/**
 * Created by jojoldu@gmail.com on 2019-01-11
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

import com.jojoldu.blogcode.querydsl.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "book",
        indexes = @Index(name = "idx_book_1", columnList = "name") // id는 클러스터인덱스라 인덱스에 자동 포함
)
public class Book extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "book_no")
    private Integer bookNo;

    @Column(name = "book_type")
    @Enumerated(EnumType.STRING)
    private BookType bookType;

    @Builder
    public Book(String name, int bookNo, BookType bookType) {
        this.name = name;
        this.bookNo = bookNo;
        this.bookType = bookType;
    }

    public void changeName (String name) {
        this.name = name;
    }

    public static Book create (String name) {
        return Book.builder()
                .name(name)
                .build();

    }

}
