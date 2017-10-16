package com.jojoldu.mockreader.batch;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 16.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Entity
@Getter
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
