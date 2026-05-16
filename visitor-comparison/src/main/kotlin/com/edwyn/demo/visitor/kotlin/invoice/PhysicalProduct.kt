package com.edwyn.demo.visitor.kotlin.invoice

data class PhysicalProduct(
    val name: String,
    val unitPriceCents: Int,
    val quantity: Int,
) : InvoiceLine
