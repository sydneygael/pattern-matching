package com.edwyn.demo.visitor.kotlin.payment

data class PaymentResult(
    val baseCents: Int,
    val adjustmentCents: Int,
    val finalCents: Int,
    val description: String,
)
