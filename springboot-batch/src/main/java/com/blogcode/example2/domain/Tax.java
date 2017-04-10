package com.blogcode.example2.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 10.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Entity
@Getter
@NoArgsConstructor
public class Tax {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private long amount;

    @Column
    private Long ownerNo;

    public Tax(long amount, Long ownerNo) {
        this.amount = amount;
        this.ownerNo = ownerNo;
    }
}
