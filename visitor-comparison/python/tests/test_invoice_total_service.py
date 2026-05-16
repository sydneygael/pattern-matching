import pytest

from visitor_comparison import CustomerType, Invoice, InvoiceTotalService, OnlineProduct, PhysicalProduct


def test_calculates_total_for_standard_invoice_with_physical_and_online_products() -> None:
    invoice = Invoice(
        customer_type=CustomerType.STANDARD,
        lines=(
            PhysicalProduct("Keyboard", unit_price_cents=4_990, quantity=2),
            OnlineProduct("Analytics license", unit_price_cents=1_299, license_count=3),
        )
    )

    total = InvoiceTotalService().calculate(invoice)

    assert total.subtotal_cents == 13_877
    assert total.discount_cents == 0
    assert total.total_cents == 13_877


def test_applies_premium_discount_only_above_threshold() -> None:
    invoice = Invoice(
        customer_type=CustomerType.PREMIUM,
        lines=(
            PhysicalProduct("Keyboard", unit_price_cents=4_990, quantity=2),
            OnlineProduct("Analytics license", unit_price_cents=1_299, license_count=3),
        )
    )

    total = InvoiceTotalService().calculate(invoice)

    assert total.subtotal_cents == 13_877
    assert total.discount_cents == 693
    assert total.total_cents == 13_184


def test_applies_vip_guarded_discount_above_threshold() -> None:
    invoice = Invoice(
        customer_type=CustomerType.VIP,
        lines=(
            PhysicalProduct("Monitor", unit_price_cents=12_000, quantity=1),
            OnlineProduct("Analytics license", unit_price_cents=2_000, license_count=4),
        )
    )

    total = InvoiceTotalService().calculate(invoice)

    assert total.subtotal_cents == 20_000
    assert total.discount_cents == 2_000
    assert total.total_cents == 18_000


def test_rejects_invoice_with_more_than_ten_products() -> None:
    invoice = Invoice(
        customer_type=CustomerType.STANDARD,
        lines=(
            PhysicalProduct("Keyboard", unit_price_cents=4_990, quantity=8),
            OnlineProduct("Analytics license", unit_price_cents=1_299, license_count=3),
        )
    )

    with pytest.raises(ValueError, match="Invoice cannot contain more than 10 products"):
        InvoiceTotalService().calculate(invoice)


def test_rejects_empty_invoice() -> None:
    invoice = Invoice(customer_type=CustomerType.STANDARD, lines=())

    with pytest.raises(ValueError, match="Invoice must contain at least one product"):
        InvoiceTotalService().calculate(invoice)
