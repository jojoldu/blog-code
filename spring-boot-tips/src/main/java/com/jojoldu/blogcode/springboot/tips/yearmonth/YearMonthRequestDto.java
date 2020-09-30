package com.jojoldu.blogcode.springboot.tips.yearmonth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * Created by jojoldu@gmail.com on 19/11/2019
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@ToString
@Getter
@NoArgsConstructor
public class YearMonthRequestDto {
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM", timezone = "Asia/Seoul")
    private YearMonth yearMonth;

    public YearMonthRequestDto(String content, YearMonth yearMonth) {
        this.content = content;
        this.yearMonth = yearMonth;
    }

    public LocalDate getLocalDate() {
        return yearMonth.atDay(1);
    }

    public LocalDateTime getLocalDateTime() {
        return yearMonth.atDay(1).atTime(0,0);
    }
}
