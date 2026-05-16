package com.edwyn.demo.visitor.java25.review;

import com.edwyn.demo.visitor.java25.order.Discount;
import com.edwyn.demo.visitor.java25.order.Order;

import java.util.ArrayList;

public final class OrderReviewService {
    private final SubtotalVisitor subtotalVisitor = new SubtotalVisitor();
    private final ValidationVisitor validationVisitor = new ValidationVisitor();
    private final InvoiceLineVisitor invoiceLineVisitor = new InvoiceLineVisitor();

    public OrderReview review(Order order) {
        var invoiceLines = new ArrayList<String>();
        var issues = new ArrayList<String>();
        var subtotalCents = 0;
        var discountCents = 0;

        for (var item : order.items()) {
            invoiceLines.add(item.accept(invoiceLineVisitor));
            issues.addAll(item.accept(validationVisitor));

            var amount = item.accept(subtotalVisitor);
            if (item instanceof Discount) {
                discountCents += -amount;
            } else {
                subtotalCents += amount;
            }
        }

        if (discountCents > subtotalCents) {
            issues.add("Discounts cannot exceed subtotal");
        }

        return new OrderReview(
            subtotalCents,
            discountCents,
            subtotalCents - discountCents,
            invoiceLines,
            issues
        );
    }
}
