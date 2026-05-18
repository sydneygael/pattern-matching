package com.edwyn.demo.visitor.java25.order;

public record ServiceLine(String name, int hourlyRateCents, int hours) implements OrderItem {}
