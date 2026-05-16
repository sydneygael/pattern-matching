package com.edwyn.demo.visitor.kotlin.review

data class OrderReview(
    val subtotalCents: Int,
    val discountCents: Int,
    val totalCents: Int,
    val invoiceLines: List<String>,
    val issues: List<String>,
)
