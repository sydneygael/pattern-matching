package com.edwyn.demo.visitor.java25.order;

public record PhysicalProduct(String name, int unitPriceCents, int quantity, int weightGrams) implements OrderItem {}
