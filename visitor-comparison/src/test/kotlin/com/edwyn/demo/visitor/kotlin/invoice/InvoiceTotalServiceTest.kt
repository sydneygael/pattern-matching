package com.edwyn.demo.visitor.kotlin.invoice

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class InvoiceTotalServiceTest {
    private val service = InvoiceTotalService()

    @Test
    fun calculatesTotalForStandardInvoiceWithPhysicalAndOnlineProducts() {
        val invoice = Invoice(
            customerType = CustomerType.STANDARD,
            lines = listOf(
                PhysicalProduct("Keyboard", unitPriceCents = 4_990, quantity = 2),
                OnlineProduct("Analytics license", unitPriceCents = 1_299, licenseCount = 3),
            ),
        )

        val total = service.calculate(invoice)

        assertThat(total.subtotalCents).isEqualTo(13_877)
        assertThat(total.discountCents).isZero()
        assertThat(total.totalCents).isEqualTo(13_877)
    }

    @Test
    fun appliesPremiumDiscountOnlyAboveThreshold() {
        val invoice = Invoice(
            customerType = CustomerType.PREMIUM,
            lines = listOf(
                PhysicalProduct("Keyboard", unitPriceCents = 4_990, quantity = 2),
                OnlineProduct("Analytics license", unitPriceCents = 1_299, licenseCount = 3),
            ),
        )

        val total = service.calculate(invoice)

        assertThat(total.subtotalCents).isEqualTo(13_877)
        assertThat(total.discountCents).isEqualTo(693)
        assertThat(total.totalCents).isEqualTo(13_184)
    }

    @Test
    fun appliesVipGuardedDiscountAboveThreshold() {
        val invoice = Invoice(
            customerType = CustomerType.VIP,
            lines = listOf(
                PhysicalProduct("Monitor", unitPriceCents = 12_000, quantity = 1),
                OnlineProduct("Analytics license", unitPriceCents = 2_000, licenseCount = 4),
            ),
        )

        val total = service.calculate(invoice)

        assertThat(total.subtotalCents).isEqualTo(20_000)
        assertThat(total.discountCents).isEqualTo(2_000)
        assertThat(total.totalCents).isEqualTo(18_000)
    }

    @Test
    fun rejectsInvoiceWithMoreThanTenProducts() {
        val invoice = Invoice(
            customerType = CustomerType.STANDARD,
            lines = listOf(
                PhysicalProduct("Keyboard", unitPriceCents = 4_990, quantity = 8),
                OnlineProduct("Analytics license", unitPriceCents = 1_299, licenseCount = 3),
            ),
        )

        assertThatThrownBy { service.calculate(invoice) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Invoice cannot contain more than 10 products")
    }

    @Test
    fun rejectsEmptyInvoice() {
        val invoice = Invoice(
            customerType = CustomerType.STANDARD,
            lines = emptyList(),
        )

        assertThatThrownBy { service.calculate(invoice) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Invoice must contain at least one product")
    }
}
