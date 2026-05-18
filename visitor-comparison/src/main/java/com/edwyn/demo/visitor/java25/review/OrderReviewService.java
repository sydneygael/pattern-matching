package com.edwyn.demo.visitor.java25.review;

import com.edwyn.demo.visitor.java25.order.Discount;
import com.edwyn.demo.visitor.java25.order.DigitalProduct;
import com.edwyn.demo.visitor.java25.order.Order;
import com.edwyn.demo.visitor.java25.order.PhysicalProduct;
import com.edwyn.demo.visitor.java25.order.ServiceLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class OrderReviewService {
    public OrderReview review(Order order) {
        var invoiceLines = new ArrayList<String>();
        var issues = new ArrayList<String>();
        var subtotalCents = 0;
        var discountCents = 0;

        for (var item : order.items()) {
            invoiceLines.add(switch (item) {
                case PhysicalProduct product -> product.quantity()
                    + " x "
                    + product.name()
                    + " = "
                    + money(product.unitPriceCents() * product.quantity());
                case DigitalProduct product -> product.licenseCount()
                    + " licenses "
                    + product.name()
                    + " = "
                    + money(product.unitPriceCents() * product.licenseCount());
                case ServiceLine service -> service.name()
                    + " ("
                    + service.hours()
                    + "h) = "
                    + money(service.hourlyRateCents() * service.hours());
                case Discount discount -> "Discount "
                    + discount.code()
                    + " = "
                    + money(-discount.amountCents());
            });

            issues.addAll(switch (item) {
                case PhysicalProduct product -> {
                    var lineIssues = new ArrayList<String>();
                    var label = label(product.name(), "Physical product");
                    requireName(product.name(), "Physical product", lineIssues);
                    requirePositive(product.unitPriceCents(), label + " unit price", lineIssues);
                    requirePositive(product.quantity(), label + " quantity", lineIssues);
                    requirePositive(product.weightGrams(), label + " weight", lineIssues);
                    yield List.copyOf(lineIssues);
                }
                case DigitalProduct product -> {
                    var lineIssues = new ArrayList<String>();
                    var label = label(product.name(), "Digital product");
                    requireName(product.name(), "Digital product", lineIssues);
                    requirePositive(product.unitPriceCents(), label + " unit price", lineIssues);
                    requirePositive(product.licenseCount(), label + " license count", lineIssues);
                    yield List.copyOf(lineIssues);
                }
                case ServiceLine service -> {
                    var lineIssues = new ArrayList<String>();
                    var label = label(service.name(), "Service");
                    requireName(service.name(), "Service", lineIssues);
                    requirePositive(service.hourlyRateCents(), label + " hourly rate", lineIssues);
                    requirePositive(service.hours(), label + " hours", lineIssues);
                    if (service.hours() > 40) {
                        lineIssues.add(label + " cannot exceed 40 hours");
                    }
                    yield List.copyOf(lineIssues);
                }
                case Discount discount -> {
                    var lineIssues = new ArrayList<String>();
                    requireName(discount.code(), "Discount code", lineIssues);
                    requirePositive(discount.amountCents(), discount.code() + " discount amount", lineIssues);
                    yield List.copyOf(lineIssues);
                }
            });

            switch (item) {
                case Discount discount -> discountCents += discount.amountCents();
                case PhysicalProduct product -> subtotalCents += product.unitPriceCents() * product.quantity();
                case DigitalProduct product -> subtotalCents += product.unitPriceCents() * product.licenseCount();
                case ServiceLine service -> subtotalCents += service.hourlyRateCents() * service.hours();
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

    private static String money(int cents) {
        return String.format(Locale.ROOT, "%.2f EUR", cents / 100.0);
    }

    private static String label(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private static void requireName(String value, String label, List<String> issues) {
        if (value == null || value.isBlank()) {
            issues.add(label + " name is required");
        }
    }

    private static void requirePositive(int value, String label, List<String> issues) {
        if (value <= 0) {
            issues.add(label + " must be positive");
        }
    }
}
