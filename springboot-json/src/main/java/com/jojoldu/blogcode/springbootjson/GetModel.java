package com.jojoldu.blogcode.springbootjson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Created by jojoldu@gmail.com on 2018-12-14
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@ToString
@Getter
@Setter
@NoArgsConstructor
public class GetModel {
    private String name;
    private LocalDate requestDate;
}
