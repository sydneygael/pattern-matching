package com.edwyn.demo.visitor.kotlin.order

data class ServiceLine(
    val name: String,
    val hourlyRateCents: Int,
    val hours: Int,
) : OrderItem
