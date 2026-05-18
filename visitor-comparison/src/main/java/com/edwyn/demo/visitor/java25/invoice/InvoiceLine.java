package com.edwyn.demo.visitor.java25.invoice;

public sealed interface InvoiceLine permits PhysicalProduct, OnlineProduct {}
