package com.edwyn.demo.visitor.kotlin.order

data class Discount(
    val code: String,
    val amountCents: Int,
) : OrderItem
