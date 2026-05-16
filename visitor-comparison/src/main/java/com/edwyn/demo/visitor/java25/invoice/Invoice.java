package com.edwyn.demo.visitor.java25.invoice;

import java.util.List;

public record Invoice(CustomerType customerType, List<InvoiceLine> lines) {
    public Invoice {
        lines = List.copyOf(lines);
    }
}
