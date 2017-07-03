package com.jojoldu.case1;

import java.time.LocalDate;

/**
 * Created by jojoldu@gmail.com on 2017. 7. 3.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public enum LimitDayType {

    D_MINUS_1(-1, "D-1"),
    D_MINUS_2(-2, "D-2"),
    D_MINUS_3(-3, "D-3"),
    D_MINUS_4(-4, "D-4"),
    D_MINUS_5(-5, "D-5"),
    D_MINUS_6(-6, "D-6"),

    D_PLUS_1(1, "D+1"),
    D_PLUS_2(2, "D+2"),
    D_PLUS_3(3, "D+3"),
    D_PLUS_4(4, "D+4"),
    D_PLUS_5(5, "D+5"),
    D_PLUS_6(6, "D+6");

    private int day;
    private String viewTitle;

    LimitDayType(int day, String viewTitle) {
        this.day = day;
        this.viewTitle = viewTitle;
    }

    public LocalDate apply(LocalDate date){
        return date.plusDays(this.day);
    }
}
