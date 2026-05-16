package com.edwyn.demo.visitor.kotlin.invoice

class InvoiceTotalService {
    fun calculate(invoice: Invoice): InvoiceTotal {
        require(invoice.lines.isNotEmpty()) { "Invoice must contain at least one product" }

        val summary = invoice.lines
            .map(::summary)
            .fold(InvoiceLineSummary(amountCents = 0, productCount = 0)) { total, line ->
                InvoiceLineSummary(
                    amountCents = total.amountCents + line.amountCents,
                    productCount = total.productCount + line.productCount,
                )
            }

        require(summary.productCount <= MAX_PRODUCTS) { "Invoice cannot contain more than 10 products" }

        val discountCents = discountCents(invoice.customerType, summary.amountCents)

        return InvoiceTotal(
            subtotalCents = summary.amountCents,
            discountCents = discountCents,
            totalCents = summary.amountCents - discountCents,
        )
    }

    fun totalCents(invoice: Invoice): Int =
        calculate(invoice).totalCents

    private fun summary(line: InvoiceLine): InvoiceLineSummary =
        when (line) {
            is PhysicalProduct -> {
                require(line.quantity > 0) { "Physical product quantity must be positive" }
                require(line.unitPriceCents > 0) { "Physical product unit price must be positive" }
                InvoiceLineSummary(line.unitPriceCents * line.quantity, line.quantity)
            }

            is OnlineProduct -> {
                require(line.licenseCount > 0) { "Online product license count must be positive" }
                require(line.unitPriceCents > 0) { "Online product unit price must be positive" }
                InvoiceLineSummary(line.unitPriceCents * line.licenseCount, line.licenseCount)
            }
        }

    private fun discountCents(customerType: CustomerType, subtotalCents: Int): Int =
        when {
            customerType == CustomerType.PREMIUM && subtotalCents >= 10_000 -> pct(subtotalCents, 5)
            customerType == CustomerType.VIP && subtotalCents >= 20_000 -> pct(subtotalCents, 10)
            customerType == CustomerType.VIP -> pct(subtotalCents, 5)
            else -> 0
        }

    private fun pct(amountCents: Int, percent: Int): Int =
        amountCents * percent / 100

    private companion object {
        const val MAX_PRODUCTS = 10
    }
}
