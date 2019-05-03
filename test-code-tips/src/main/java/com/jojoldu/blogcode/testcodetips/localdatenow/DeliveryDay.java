package com.jojoldu.blogcode.testcodetips.localdatenow;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class DeliveryDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private boolean isHoliday;

    @Builder
    public DeliveryDay(LocalDate date, boolean isHoliday) {
        this.date = date;
        this.isHoliday = isWeekend(date) || isHoliday;
    }

    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public boolean isBusinessDay() {
        return !isHoliday;
    }

    public boolean isAfter (LocalDate compare) {
        return this.date.isAfter(compare);
    }
}
