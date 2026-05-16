package com.edwyn.demo.visitor.java25.review;

import com.edwyn.demo.visitor.java25.order.DigitalProduct;
import com.edwyn.demo.visitor.java25.order.Discount;
import com.edwyn.demo.visitor.java25.order.OrderItemVisitor;
import com.edwyn.demo.visitor.java25.order.PhysicalProduct;
import com.edwyn.demo.visitor.java25.order.ServiceLine;

import java.util.ArrayList;
import java.util.List;

public final class ValidationVisitor implements OrderItemVisitor<List<String>> {
    @Override
    public List<String> visitPhysicalProduct(PhysicalProduct item) {
        var issues = new ArrayList<String>();
        var label = label(item.name(), "Physical product");
        requireName(item.name(), "Physical product", issues);
        requirePositive(item.unitPriceCents(), label + " unit price", issues);
        requirePositive(item.quantity(), label + " quantity", issues);
        requirePositive(item.weightGrams(), label + " weight", issues);
        return List.copyOf(issues);
    }

    @Override
    public List<String> visitDigitalProduct(DigitalProduct item) {
        var issues = new ArrayList<String>();
        var label = label(item.name(), "Digital product");
        requireName(item.name(), "Digital product", issues);
        requirePositive(item.unitPriceCents(), label + " unit price", issues);
        requirePositive(item.licenseCount(), label + " license count", issues);
        return List.copyOf(issues);
    }

    @Override
    public List<String> visitServiceLine(ServiceLine item) {
        var issues = new ArrayList<String>();
        var label = label(item.name(), "Service");
        requireName(item.name(), "Service", issues);
        requirePositive(item.hourlyRateCents(), label + " hourly rate", issues);
        requirePositive(item.hours(), label + " hours", issues);
        if (item.hours() > 40) {
            issues.add(label + " cannot exceed 40 hours");
        }
        return List.copyOf(issues);
    }

    @Override
    public List<String> visitDiscount(Discount item) {
        var issues = new ArrayList<String>();
        requireName(item.code(), "Discount code", issues);
        requirePositive(item.amountCents(), item.code() + " discount amount", issues);
        return List.copyOf(issues);
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
