package com.edwyn.demo.visitor.kotlin.order

data class Order(
    val customerId: String,
    val items: List<OrderItem>,
)
