package com.edwyn.demo.visitor.kotlin.invoice

data class InvoiceTotal(
    val subtotalCents: Int,
    val discountCents: Int,
    val totalCents: Int,
)
