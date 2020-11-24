package com.jojoldu.blogcode.querydsl.domain.order;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 22/11/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Pay {
    private String code;
    private Long amount;
    private List<PayDetail> details = new ArrayList<>();

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayDetail {
        private String salesType;
        private Long amount;
    }
}
