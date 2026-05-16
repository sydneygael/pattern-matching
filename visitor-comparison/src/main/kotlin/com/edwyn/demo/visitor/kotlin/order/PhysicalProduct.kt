package com.edwyn.demo.visitor.kotlin.order

data class PhysicalProduct(
    val name: String,
    val unitPriceCents: Int,
    val quantity: Int,
    val weightGrams: Int,
) : OrderItem
