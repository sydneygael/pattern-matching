package com.edwyn.demo.visitor.java25.order;

public record DigitalProduct(
        String name,
        int unitPriceCents,
        int licenseCount) implements OrderItem {
    @Override
    public <R> R accept(OrderItemVisitor<R> visitor) {
        return visitor.visitDigitalProduct(this);
    }
}
