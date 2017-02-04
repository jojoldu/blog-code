package com.blogcode.old;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by jojoldu@gmail.com on 2017-02-04
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Entity
public class TradingStatement {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String company;

    @Column
    private String memo;

}
