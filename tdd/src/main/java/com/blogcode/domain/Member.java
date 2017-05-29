package com.blogcode.domain;

import com.blogcode.exception.NotBeNegativeException;

import java.time.LocalDate;

/**
 * Created by jojoldu@gmail.com on 2017. 5. 27.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class Member {
    private final int year;
    private final int month;
    private final int day;

    public Member(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int calculateAge() {
        verify();
        int currentYear = LocalDate.now().getYear();
        return currentYear - year+1;
    }
    
    private void verify() {
        if(year < 1 || month < 1 || day < 1){
            throw new NotBeNegativeException();
        }
    }
}
