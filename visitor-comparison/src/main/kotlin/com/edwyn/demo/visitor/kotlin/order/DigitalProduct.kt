package com.edwyn.demo.visitor.kotlin.order

data class DigitalProduct(
    val name: String,
    val unitPriceCents: Int,
    val licenseCount: Int,
) : OrderItem
