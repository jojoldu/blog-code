package com.jojoldu.blogcode.querydsl.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NegativeOrZero;

/**
 * Created by jojoldu@gmail.com on 2019-01-11
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
@NoArgsConstructor
public class StudentCount {
    private String academyName;
    private long studentCount;

}

