package com.jojoldu.why.domain.book;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 1.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Entity
@NoArgsConstructor
@Getter
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private long price;
    private Long managerId;

    public Book(String name, long price, Long managerId) {
        this.name = name;
        this.price = price;
        this.managerId = managerId;
    }
}
