package com.jojoldu.blogcode.springboot.tips.yearmonth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDate.parse;

/**
 * Created by jojoldu@gmail.com on 19/11/2019
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@ToString
@Getter
@NoArgsConstructor
public class YearMonthRequestDto {

    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM", timezone = "Asia/Seoul")
    private YearMonth yearMonth;

    @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])$", message = "년월 형식(yyyy-MM)에 맞지 않습니다")
    private String strYearMonth;

    @Builder
    public YearMonthRequestDto(YearMonth yearMonth, String strYearMonth) {
        this.yearMonth = yearMonth;
        this.strYearMonth = strYearMonth;
    }

    public String getBeforeMonthByString () {
        String strDate = this.strYearMonth+"-01";
        LocalDate date = parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate beforeDate = date.minusMonths(1);

        return beforeDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public YearMonth getBeforeMonthByYearMonth () {
        return this.yearMonth.minusMonths(1);
    }

    public LocalDate getLocalDate() {
        return yearMonth.atDay(1);
    }

    public LocalDateTime getLocalDateTime() {
        return yearMonth.atDay(1).atTime(0,0);
    }
}
