package com.edwyn.demo.visitor.java25.payment;

public sealed interface PaymentMethod permits CardPayment, CryptoPayment {}
