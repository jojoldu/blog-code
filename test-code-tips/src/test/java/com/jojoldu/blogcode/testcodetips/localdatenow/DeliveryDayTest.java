package com.jojoldu.blogcode.testcodetips.localdatenow;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeliveryDayTest {

    @Test
    public void 배송도착일자_계산_공휴일은_제외한다_now() {
        //given
        LocalDate date = LocalDate.now();

        List<DeliveryDay> days = Arrays.asList(
                new DeliveryDay(date.plusDays(1), false),
                new DeliveryDay(date.plusDays(2), false),
                new DeliveryDay(date.plusDays(3), false)
        );

        DeliveryDayCalendar calendar = new DeliveryDayCalendar(days);

        //when
        DeliveryDay expectedArriveDate = calendar.getExpectedArriveDate(date);

        //then
        assertThat(expectedArriveDate.getDate()).isEqualTo(date.plusDays(3));
    }

    @Test
    public void 배송도착일자_계산_공휴일은_제외한다_of() {
        //given
        LocalDate date = LocalDate.of(2019,5,7);

        List<DeliveryDay> days = Arrays.asList(
                new DeliveryDay(date.plusDays(1), false),
                new DeliveryDay(date.plusDays(2), false),
                new DeliveryDay(date.plusDays(3), false)
        );

        DeliveryDayCalendar calendar = new DeliveryDayCalendar(days);

        //when
        DeliveryDay expectedArriveDate = calendar.getExpectedArriveDate(date);

        //then
        assertThat(expectedArriveDate.getDate()).isEqualTo(date.plusDays(3));
    }

}
