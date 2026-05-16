package com.edwyn.demo.visitor.kotlin.invoice

data class Invoice(
    val customerType: CustomerType,
    val lines: List<InvoiceLine>,
)
