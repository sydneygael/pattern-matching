package com.edwyn.demo.visitor.kotlin.invoice

data class OnlineProduct(
    val name: String,
    val unitPriceCents: Int,
    val licenseCount: Int,
) : InvoiceLine
