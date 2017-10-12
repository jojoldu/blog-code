package com.jojoldu.h2.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 12.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Entity
@Getter
@NoArgsConstructor
public class Shop {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String address;

    public Shop(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
