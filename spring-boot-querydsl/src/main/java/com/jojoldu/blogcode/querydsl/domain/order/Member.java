package com.jojoldu.blogcode.querydsl.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * Created by jojoldu@gmail.com on 27/11/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Member {

    private String memberNo;
    private String memberName;
}
