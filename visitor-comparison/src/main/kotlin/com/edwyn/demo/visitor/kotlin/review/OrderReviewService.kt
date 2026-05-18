package com.edwyn.demo.visitor.kotlin.review

import com.edwyn.demo.visitor.kotlin.order.DigitalProduct
import com.edwyn.demo.visitor.kotlin.order.Discount
import com.edwyn.demo.visitor.kotlin.order.Order
import com.edwyn.demo.visitor.kotlin.order.PhysicalProduct
import com.edwyn.demo.visitor.kotlin.order.ServiceLine
import java.util.Locale

class OrderReviewService {
    fun review(order: Order): OrderReview {
        val invoiceLines = mutableListOf<String>()
        val issues = mutableListOf<String>()
        var subtotalCents = 0
        var discountCents = 0

        order.items.forEach { item ->
            invoiceLines += when (item) {
                is PhysicalProduct ->
                    "${item.quantity} x ${item.name} = ${formatMoney(item.unitPriceCents * item.quantity)}"

                is DigitalProduct ->
                    "${item.licenseCount} licenses ${item.name} = ${formatMoney(item.unitPriceCents * item.licenseCount)}"

                is ServiceLine ->
                    "${item.name} (${item.hours}h) = ${formatMoney(item.hourlyRateCents * item.hours)}"

                is Discount ->
                    "Discount ${item.code} = ${formatMoney(-item.amountCents)}"
            }

            issues += when (item) {
                is PhysicalProduct -> buildList {
                    val label = item.name.ifBlank { "Physical product" }
                    validateName(item.name, "Physical product")
                    validatePositive(item.unitPriceCents, "$label unit price")
                    validatePositive(item.quantity, "$label quantity")
                    validatePositive(item.weightGrams, "$label weight")
                }

                is DigitalProduct -> buildList {
                    val label = item.name.ifBlank { "Digital product" }
                    validateName(item.name, "Digital product")
                    validatePositive(item.unitPriceCents, "$label unit price")
                    validatePositive(item.licenseCount, "$label license count")
                }

                is ServiceLine -> buildList {
                    val label = item.name.ifBlank { "Service" }
                    validateName(item.name, "Service")
                    validatePositive(item.hourlyRateCents, "$label hourly rate")
                    validatePositive(item.hours, "$label hours")
                    if (item.hours > 40) {
                        add("$label cannot exceed 40 hours")
                    }
                }

                is Discount -> buildList {
                    validateName(item.code, "Discount code")
                    validatePositive(item.amountCents, "${item.code} discount amount")
                }
            }

            when (item) {
                is Discount -> discountCents += item.amountCents
                is PhysicalProduct -> subtotalCents += item.unitPriceCents * item.quantity
                is DigitalProduct -> subtotalCents += item.unitPriceCents * item.licenseCount
                is ServiceLine -> subtotalCents += item.hourlyRateCents * item.hours
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

    private fun formatMoney(cents: Int): String =
        String.format(Locale.ROOT, "%.2f EUR", cents / 100.0)

    private fun MutableList<String>.validateName(value: String, label: String) {
        if (value.isBlank()) {
            add("$label name is required")
        }
    }

    private fun MutableList<String>.validatePositive(value: Int, label: String) {
        if (value <= 0) {
            add("$label must be positive")
        }
    }
}
