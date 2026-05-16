package com.edwyn.demo.visitor.java25.order;

public record Discount(
        String code,
        int amountCents) implements OrderItem {
    @Override
    public <R> R accept(OrderItemVisitor<R> visitor) {
        return visitor.visitDiscount(this);
    }
}
