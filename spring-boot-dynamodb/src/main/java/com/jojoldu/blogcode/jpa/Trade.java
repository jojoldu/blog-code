package com.jojoldu.blogcode.jpa;

/**
 * Created by jojoldu@gmail.com on 05/03/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long amount;
    private String tradeName;

    @Builder
    public Trade(long amount, String tradeName) {
        this.amount = amount;
        this.tradeName = tradeName;
    }
}
