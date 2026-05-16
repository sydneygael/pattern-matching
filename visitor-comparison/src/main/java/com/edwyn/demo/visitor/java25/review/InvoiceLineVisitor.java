package com.edwyn.demo.visitor.java25.review;

import com.edwyn.demo.visitor.java25.order.DigitalProduct;
import com.edwyn.demo.visitor.java25.order.Discount;
import com.edwyn.demo.visitor.java25.order.OrderItemVisitor;
import com.edwyn.demo.visitor.java25.order.PhysicalProduct;
import com.edwyn.demo.visitor.java25.order.ServiceLine;

import java.util.Locale;

public final class InvoiceLineVisitor implements OrderItemVisitor<String> {
    @Override
    public String visitPhysicalProduct(PhysicalProduct item) {
        return item.quantity() + " x " + item.name() + " = " + money(item.unitPriceCents() * item.quantity());
    }

    @Override
    public String visitDigitalProduct(DigitalProduct item) {
        return item.licenseCount() + " licenses " + item.name() + " = " + money(item.unitPriceCents() * item.licenseCount());
    }

    @Override
    public String visitServiceLine(ServiceLine item) {
        return item.name() + " (" + item.hours() + "h) = " + money(item.hourlyRateCents() * item.hours());
    }

    @Override
    public String visitDiscount(Discount item) {
        return "Discount " + item.code() + " = " + money(-item.amountCents());
    }

    private static String money(int cents) {
        return String.format(Locale.ROOT, "%.2f EUR", cents / 100.0);
    }
}
