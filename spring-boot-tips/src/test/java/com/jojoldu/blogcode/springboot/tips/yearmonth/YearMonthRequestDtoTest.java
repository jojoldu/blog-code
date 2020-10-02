package com.jojoldu.blogcode.springboot.tips.yearmonth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.time.temporal.IsoFields;

import static java.time.YearMonth.of;
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
        YearMonthRequestDto dto = new YearMonthRequestDto(of(2020,9));

        //whens
        YearMonth beforeMonth = dto.getBeforeMonthByYearMonth();

        //then
        assertThat(beforeMonth).isEqualTo(of(2020,8));
    }

    @Test
    void YearMonth로_분기를_구할수_있다() throws Exception {
        assertThat(YearMonth.of(2020,1).get(IsoFields.QUARTER_OF_YEAR)).isEqualTo(1);
        assertThat(YearMonth.of(2020,4).get(IsoFields.QUARTER_OF_YEAR)).isEqualTo(2);
        assertThat(YearMonth.of(2020,7).get(IsoFields.QUARTER_OF_YEAR)).isEqualTo(3);
        assertThat(YearMonth.of(2020,10).get(IsoFields.QUARTER_OF_YEAR)).isEqualTo(4);
    }
}
