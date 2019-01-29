package com.jojoldu.blogcode.querydsl.dto;

import com.jojoldu.blogcode.querydsl.domain.pointevent.PointStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by jojoldu@gmail.com on 2019-01-30
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
@NoArgsConstructor
public class PointCalculateAmount {
    private PointStatus pointStatus;
    private long pointAmount;
}
