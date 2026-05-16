package com.edwyn.demo.visitor.java25.order;

public interface OrderItemVisitor<R> {
    R visitPhysicalProduct(PhysicalProduct item);

    R visitDigitalProduct(DigitalProduct item);

    R visitServiceLine(ServiceLine item);

    R visitDiscount(Discount item);
}
