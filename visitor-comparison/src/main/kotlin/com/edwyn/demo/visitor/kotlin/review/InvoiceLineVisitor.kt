package com.edwyn.demo.visitor.kotlin.review

import com.edwyn.demo.visitor.kotlin.order.DigitalProduct
import com.edwyn.demo.visitor.kotlin.order.Discount
import com.edwyn.demo.visitor.kotlin.order.OrderItem
import com.edwyn.demo.visitor.kotlin.order.PhysicalProduct
import com.edwyn.demo.visitor.kotlin.order.ServiceLine
import java.util.Locale

class InvoiceLineVisitor {
    fun visit(item: OrderItem): String =
        when (item) {
            is PhysicalProduct -> "${item.quantity} x ${item.name} = ${formatMoney(item.unitPriceCents * item.quantity)}"
            is DigitalProduct -> "${item.licenseCount} licenses ${item.name} = ${formatMoney(item.unitPriceCents * item.licenseCount)}"
            is ServiceLine -> "${item.name} (${item.hours}h) = ${formatMoney(item.hourlyRateCents * item.hours)}"
            is Discount -> "Discount ${item.code} = ${formatMoney(-item.amountCents)}"
        }

    private fun formatMoney(cents: Int): String =
        String.format(Locale.ROOT, "%.2f EUR", cents / 100.0)
}
