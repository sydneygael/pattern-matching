package com.edwyn.demo.visitor.java25.customer;

public sealed interface Customer permits Standard, Vip, Premium {
    String id();
}
