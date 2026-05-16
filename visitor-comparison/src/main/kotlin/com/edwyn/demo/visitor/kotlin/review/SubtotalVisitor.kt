package com.edwyn.demo.visitor.kotlin.review

import com.edwyn.demo.visitor.kotlin.order.DigitalProduct
import com.edwyn.demo.visitor.kotlin.order.Discount
import com.edwyn.demo.visitor.kotlin.order.OrderItem
import com.edwyn.demo.visitor.kotlin.order.PhysicalProduct
import com.edwyn.demo.visitor.kotlin.order.ServiceLine

class SubtotalVisitor {
    fun visit(item: OrderItem): Int =
        when (item) {
            is PhysicalProduct -> item.unitPriceCents * item.quantity
            is DigitalProduct -> item.unitPriceCents * item.licenseCount
            is ServiceLine -> item.hourlyRateCents * item.hours
            is Discount -> -item.amountCents
        }
}
