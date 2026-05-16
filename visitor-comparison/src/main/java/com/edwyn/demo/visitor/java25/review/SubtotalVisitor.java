package com.edwyn.demo.visitor.java25.review;

import com.edwyn.demo.visitor.java25.order.DigitalProduct;
import com.edwyn.demo.visitor.java25.order.Discount;
import com.edwyn.demo.visitor.java25.order.OrderItemVisitor;
import com.edwyn.demo.visitor.java25.order.PhysicalProduct;
import com.edwyn.demo.visitor.java25.order.ServiceLine;

public final class SubtotalVisitor implements OrderItemVisitor<Integer> {
    @Override
    public Integer visitPhysicalProduct(PhysicalProduct item) {
        return item.unitPriceCents() * item.quantity();
    }

    @Override
    public Integer visitDigitalProduct(DigitalProduct item) {
        return item.unitPriceCents() * item.licenseCount();
    }

    @Override
    public Integer visitServiceLine(ServiceLine item) {
        return item.hourlyRateCents() * item.hours();
    }

    @Override
    public Integer visitDiscount(Discount item) {
        return -item.amountCents();
    }
}
