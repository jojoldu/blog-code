package com.jojoldu.blogcode.springboot.tips.validatemessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by jojoldu@gmail.com on 09/01/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    IN_PROGRESS ("진행중"),
    COMPLETE ("종료"),
    CANCEL ("취소");

    private final String message;
}
