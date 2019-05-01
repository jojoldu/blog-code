package com.jojoldu.blogcode.testcodetips.localdatenow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeliveryDayCalendar {
    private static final int CYCLE_DAY = 3;

    private final List<DeliveryDay> candidates;

    public DeliveryDayCalendar(List<DeliveryDay> candidates) {
        this.candidates = candidates;
    }

    public LocalDate getExpectedArriveDate(LocalDate orderDate) {
        List<LocalDate> businessDates = candidates.stream()
                .filter(d -> d.isAfter(orderDate))
                .filter(DeliveryDay::isBusinessDay)
                .map(DeliveryDay::getDate)
                .collect(Collectors.toList());

        return businessDates.get(CYCLE_DAY-1);
    }
}
