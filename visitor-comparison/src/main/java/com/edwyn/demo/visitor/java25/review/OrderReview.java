package com.edwyn.demo.visitor.java25.review;

import java.util.List;

public record OrderReview(
    int subtotalCents,
    int discountCents,
    int totalCents,
    List<String> invoiceLines,
    List<String> issues
) {
    public OrderReview {
        invoiceLines = List.copyOf(invoiceLines);
        issues = List.copyOf(issues);
    }
}
