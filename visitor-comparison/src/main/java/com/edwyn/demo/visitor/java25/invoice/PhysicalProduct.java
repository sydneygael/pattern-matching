package com.edwyn.demo.visitor.java25.invoice;

public record PhysicalProduct(String name, int unitPriceCents, int quantity) implements InvoiceLine {
    @Override
    public <R> R accept(InvoiceLineVisitor<R> visitor) {
        return visitor.visitPhysicalProduct(this);
    }
}
