package com.edwyn.demo.visitor.java25.payment;

public record CryptoPayment(String currency) implements PaymentMethod {}
