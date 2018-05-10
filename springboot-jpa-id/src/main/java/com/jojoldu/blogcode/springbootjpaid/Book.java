package com.jojoldu.blogcode.springbootjpaid;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by jojoldu@gmail.com on 2018. 5. 10.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;
    private String title;

    public Book(String title) {
        this.title = title;
    }
}
