package com.edwyn.demo.visitor.java25.invoice;

public record OnlineProduct(String name, int unitPriceCents, int licenseCount) implements InvoiceLine {
    @Override
    public <R> R accept(InvoiceLineVisitor<R> visitor) {
        return visitor.visitOnlineProduct(this);
    }
}
