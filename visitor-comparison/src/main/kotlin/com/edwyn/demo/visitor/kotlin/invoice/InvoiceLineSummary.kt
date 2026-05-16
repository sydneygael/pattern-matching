package com.edwyn.demo.visitor.kotlin.invoice

data class InvoiceLineSummary(
    val amountCents: Int,
    val productCount: Int,
)
