package com.edwyn.demo.visitor.kotlin.review

import com.edwyn.demo.visitor.kotlin.order.DigitalProduct
import com.edwyn.demo.visitor.kotlin.order.Discount
import com.edwyn.demo.visitor.kotlin.order.OrderItem
import com.edwyn.demo.visitor.kotlin.order.PhysicalProduct
import com.edwyn.demo.visitor.kotlin.order.ServiceLine

class ValidationVisitor {
    fun visit(item: OrderItem): List<String> =
        when (item) {
            is PhysicalProduct -> validatePhysicalProduct(item)
            is DigitalProduct -> validateDigitalProduct(item)
            is ServiceLine -> validateServiceLine(item)
            is Discount -> validateDiscount(item)
        }

    private fun validatePhysicalProduct(item: PhysicalProduct): List<String> = buildList {
        val label = item.name.ifBlank { "Physical product" }
        validateName(item.name, "Physical product")
        validatePositive(item.unitPriceCents, "$label unit price")
        validatePositive(item.quantity, "$label quantity")
        validatePositive(item.weightGrams, "$label weight")
    }

    private fun validateDigitalProduct(item: DigitalProduct): List<String> = buildList {
        val label = item.name.ifBlank { "Digital product" }
        validateName(item.name, "Digital product")
        validatePositive(item.unitPriceCents, "$label unit price")
        validatePositive(item.licenseCount, "$label license count")
    }

    private fun validateServiceLine(item: ServiceLine): List<String> = buildList {
        val label = item.name.ifBlank { "Service" }
        validateName(item.name, "Service")
        validatePositive(item.hourlyRateCents, "$label hourly rate")
        validatePositive(item.hours, "$label hours")
        validateMaxHours(item.hours, label, maxHours = 40)
    }

    private fun validateDiscount(item: Discount): List<String> = buildList {
        validateName(item.code, "Discount code")
        validatePositive(item.amountCents, "${item.code} discount amount")
    }

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

    private fun MutableList<String>.validateMaxHours(hours: Int, label: String, maxHours: Int) {
        if (hours > maxHours) {
            add("$label cannot exceed $maxHours hours")
        }
    }
}
