package com.jojoldu.case4;

import com.github.jojoldu.mapper.EnumMapperType;

/**
 * Created by jojoldu@gmail.com on 2017. 7. 2.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public enum FeeType implements EnumMapperType{
    PERCENT("정율"),
    MONEY("정액");

    private String title;

    FeeType(String title) {
        this.title = title;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getTitle() {
        return title;
    }
}
