from __future__ import annotations

from dataclasses import dataclass
from enum import Enum
from typing import TYPE_CHECKING, Sequence

if TYPE_CHECKING:
    from visitor_comparison.protocols import InvoiceLine, InvoiceLineVisitor, R


class CustomerType(Enum):
    STANDARD = "STANDARD"
    PREMIUM = "PREMIUM"
    VIP = "VIP"


@dataclass(frozen=True)
class PhysicalProduct:
    name: str
    unit_price_cents: int
    quantity: int

    def accept(self, visitor: InvoiceLineVisitor[R]) -> R:
        return visitor.visit_physical_product(self)


@dataclass(frozen=True)
class OnlineProduct:
    name: str
    unit_price_cents: int
    license_count: int

    def accept(self, visitor: InvoiceLineVisitor[R]) -> R:
        return visitor.visit_online_product(self)


@dataclass(frozen=True)
class Invoice:
    customer_type: CustomerType
    lines: Sequence[InvoiceLine]


@dataclass(frozen=True)
class InvoiceTotal:
    subtotal_cents: int
    discount_cents: int
    total_cents: int


@dataclass(frozen=True)
class InvoiceLineSummary:
    amount_cents: int
    product_count: int
