package com.edwyn.demo.visitor.java25.invoice;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InvoiceTotalServiceTest {
    private final InvoiceTotalService service = new InvoiceTotalService();

    @Test
    void calculatesTotalForStandardInvoiceWithPhysicalAndOnlineProducts() {
        var invoice = new Invoice(CustomerType.STANDARD, List.of(
            new PhysicalProduct("Keyboard", 4_990, 2),
            new OnlineProduct("Analytics license", 1_299, 3)
        ));

        var total = service.calculate(invoice);

        assertThat(total.subtotalCents()).isEqualTo(13_877);
        assertThat(total.discountCents()).isZero();
        assertThat(total.totalCents()).isEqualTo(13_877);
    }

    @Test
    void appliesPremiumDiscountOnlyAboveThreshold() {
        var invoice = new Invoice(CustomerType.PREMIUM, List.of(
            new PhysicalProduct("Keyboard", 4_990, 2),
            new OnlineProduct("Analytics license", 1_299, 3)
        ));

        var total = service.calculate(invoice);

        assertThat(total.subtotalCents()).isEqualTo(13_877);
        assertThat(total.discountCents()).isEqualTo(693);
        assertThat(total.totalCents()).isEqualTo(13_184);
    }

    @Test
    void appliesVipGuardedDiscountAboveThreshold() {
        var invoice = new Invoice(CustomerType.VIP, List.of(
            new PhysicalProduct("Monitor", 12_000, 1),
            new OnlineProduct("Analytics license", 2_000, 4)
        ));

        var total = service.calculate(invoice);

        assertThat(total.subtotalCents()).isEqualTo(20_000);
        assertThat(total.discountCents()).isEqualTo(2_000);
        assertThat(total.totalCents()).isEqualTo(18_000);
    }

    @Test
    void rejectsInvoiceWithMoreThanTenProducts() {
        var invoice = new Invoice(CustomerType.STANDARD, List.of(
            new PhysicalProduct("Keyboard", 4_990, 8),
            new OnlineProduct("Analytics license", 1_299, 3)
        ));

        assertThatThrownBy(() -> service.calculate(invoice))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invoice cannot contain more than 10 products");
    }

    @Test
    void rejectsEmptyInvoice() {
        var invoice = new Invoice(CustomerType.STANDARD, List.of());

        assertThatThrownBy(() -> service.calculate(invoice))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invoice must contain at least one product");
    }
}
