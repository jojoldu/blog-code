package com.jojoldu.blogcode.querydsl.domain.pointevent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by jojoldu@gmail.com on 2019-01-30
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
@RequiredArgsConstructor
public enum PointStatus {
    EARN,
    USE,
    USE_CANCEL
}
