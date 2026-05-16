package com.edwyn.demo.visitor.java25.payment;

public record CardPayment(String network) implements PaymentMethod {}
