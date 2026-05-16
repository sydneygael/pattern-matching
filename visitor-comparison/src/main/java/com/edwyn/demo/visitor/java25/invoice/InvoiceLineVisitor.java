package com.edwyn.demo.visitor.java25.invoice;

public interface InvoiceLineVisitor<R> {
    R visitPhysicalProduct(PhysicalProduct line);

    R visitOnlineProduct(OnlineProduct line);
}
