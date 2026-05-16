from visitor_comparison.models import (
    CustomerType,
    Invoice,
    InvoiceLineSummary,
    InvoiceTotal,
    OnlineProduct,
    PhysicalProduct,
)


class InvoiceTotalService:
    MAX_PRODUCTS = 10

    def calculate(self, invoice: Invoice) -> InvoiceTotal:
        if not invoice.lines:
            raise ValueError("Invoice must contain at least one product")

        summary = InvoiceLineSummary(amount_cents=0, product_count=0)
        for line in invoice.lines:
            line_summary = line.accept(self)
            summary = InvoiceLineSummary(
                amount_cents=summary.amount_cents + line_summary.amount_cents,
                product_count=summary.product_count + line_summary.product_count,
            )

        if summary.product_count > self.MAX_PRODUCTS:
            raise ValueError("Invoice cannot contain more than 10 products")

        discount_cents = self._discount_cents(invoice.customer_type, summary.amount_cents)

        return InvoiceTotal(
            subtotal_cents=summary.amount_cents,
            discount_cents=discount_cents,
            total_cents=summary.amount_cents - discount_cents,
        )

    def total_cents(self, invoice: Invoice) -> int:
        return self.calculate(invoice).total_cents

    def visit_physical_product(self, line: PhysicalProduct) -> InvoiceLineSummary:
        self._guard_positive(line.quantity, "Physical product quantity must be positive")
        self._guard_positive(line.unit_price_cents, "Physical product unit price must be positive")
        return InvoiceLineSummary(
            amount_cents=line.unit_price_cents * line.quantity,
            product_count=line.quantity,
        )

    def visit_online_product(self, line: OnlineProduct) -> InvoiceLineSummary:
        self._guard_positive(line.license_count, "Online product license count must be positive")
        self._guard_positive(line.unit_price_cents, "Online product unit price must be positive")
        return InvoiceLineSummary(
            amount_cents=line.unit_price_cents * line.license_count,
            product_count=line.license_count,
        )

    def _discount_cents(self, customer_type: CustomerType, subtotal_cents: int) -> int:
        match customer_type:
            case CustomerType.PREMIUM if subtotal_cents >= 10_000:
                return self._pct(subtotal_cents, 5)
            case CustomerType.VIP if subtotal_cents >= 20_000:
                return self._pct(subtotal_cents, 10)
            case CustomerType.VIP:
                return self._pct(subtotal_cents, 5)
            case _:
                return 0

    @staticmethod
    def _guard_positive(value: int, message: str) -> None:
        if value <= 0:
            raise ValueError(message)

    @staticmethod
    def _pct(amount_cents: int, percent: int) -> int:
        return amount_cents * percent // 100
