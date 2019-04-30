package com.jojoldu.blogcode.testcodetips.localdatenow;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DeliveryDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate expectedArriveDate;

    private boolean isHoliday;

    @Builder
    public DeliveryDay(LocalDate expectedArriveDate, boolean isHoliday) {
        this.expectedArriveDate = expectedArriveDate;
        this.isHoliday = isHoliday;
    }
}
