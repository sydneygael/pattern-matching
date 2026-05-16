package com.edwyn.demo.visitor.kotlin.review

import com.edwyn.demo.visitor.kotlin.order.Discount
import com.edwyn.demo.visitor.kotlin.order.Order

class OrderReviewService {
    private val subtotalVisitor = SubtotalVisitor()
    private val validationVisitor = ValidationVisitor()
    private val invoiceLineVisitor = InvoiceLineVisitor()

    fun review(order: Order): OrderReview {
        val invoiceLines = mutableListOf<String>()
        val issues = mutableListOf<String>()
        var subtotalCents = 0
        var discountCents = 0

        order.items.forEach { item ->
            invoiceLines += invoiceLineVisitor.visit(item)
            issues += validationVisitor.visit(item)

            val amount = subtotalVisitor.visit(item)
            if (item is Discount) {
                discountCents += -amount
            } else {
                subtotalCents += amount
            }
        }

        if (discountCents > subtotalCents) {
            issues += "Discounts cannot exceed subtotal"
        }

        return OrderReview(
            subtotalCents = subtotalCents,
            discountCents = discountCents,
            totalCents = subtotalCents - discountCents,
            invoiceLines = invoiceLines,
            issues = issues,
        )
    }
}
