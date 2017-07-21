package com.blogcode.jpa.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by jojoldu@gmail.com on 2017. 7. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Entity
@Getter
@NoArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Teacher(String name) {
        this.name = name;
    }
}
