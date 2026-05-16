package com.edwyn.demo.visitor.java25.order;

import java.util.List;

public record Order(
        String customerId,
        List<OrderItem> items) {
    public Order {
        items = List.copyOf(items);
    }
}
