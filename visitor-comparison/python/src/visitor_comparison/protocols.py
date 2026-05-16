from __future__ import annotations

from typing import TYPE_CHECKING, Generic, Protocol, TypeVar

if TYPE_CHECKING:
    from visitor_comparison.models import OnlineProduct, PhysicalProduct

R = TypeVar("R")


class InvoiceLineVisitor(Protocol, Generic[R]):
    def visit_physical_product(self, line: PhysicalProduct) -> R:
        ...

    def visit_online_product(self, line: OnlineProduct) -> R:
        ...


class InvoiceLine(Protocol):
    def accept(self, visitor: InvoiceLineVisitor[R]) -> R:
        ...
