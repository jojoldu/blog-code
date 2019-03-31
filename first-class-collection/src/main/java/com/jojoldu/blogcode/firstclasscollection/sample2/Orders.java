package com.jojoldu.blogcode.firstclasscollection.sample2;

import java.util.List;

public class Orders {
    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public long getAmountSum() {
        return orders.stream()
                .mapToLong(Order::getAmount)
                .sum();
    }
}
