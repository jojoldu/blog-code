package com.jojoldu.blogcode.springboot.tips.yearmonth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 01/10/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@ExtendWith(MockitoExtension.class)
public class YearMonthRequestDtoTest {

    @Test
    void 문자열로_직전월을_구한다() throws Exception {
        //given
        String month = "2020-09";
        YearMonthRequestDto dto = YearMonthRequestDto.builder()
                .strYearMonth(month)
                .build();
        //when
        String beforeMonth = dto.getBeforeMonthByString();

        //then
        assertThat(beforeMonth).isEqualTo("2020-08");
    }

    @Test
    void YearMonth로_직전월을_구한다() throws Exception {
        //given
        YearMonth month = YearMonth.of(2020,9);
        YearMonthRequestDto dto = YearMonthRequestDto.builder()
                .yearMonth(month)
                .build();
        //when
        YearMonth beforeMonth = dto.getBeforeMonthByYearMonth();

        //then
        assertThat(beforeMonth).isEqualTo(YearMonth.of(2020,8));
    }
}
