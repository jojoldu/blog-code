package com.jojoldu.why.domain.manager;

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

@Getter
@NoArgsConstructor
@Entity
public class Manager {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Manager(String name) {
        this.name = name;
    }
}
