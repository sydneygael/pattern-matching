package com.edwyn.demo.visitor.java25.payment;

public record PaymentResult(int baseCents, int adjustmentCents, int finalCents, String description) {}
