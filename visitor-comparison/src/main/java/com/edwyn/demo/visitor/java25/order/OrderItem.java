package com.edwyn.demo.visitor.java25.order;

public sealed interface OrderItem permits PhysicalProduct, DigitalProduct, ServiceLine, Discount {}
