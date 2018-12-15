package com.jojoldu.blogcode.springbootjson;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by jojoldu@gmail.com on 2018-12-14
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
@RequiredArgsConstructor
public class ResponseModel {
    private final String name;
    private final LocalDateTime requestDateTime;
}
