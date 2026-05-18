package com.edwyn.demo.visitor.java25.invoice;

public final class InvoiceTotalService {
    private static final int MAX_PRODUCTS = 10;

    public int totalCents(Invoice invoice) {
        return calculate(invoice).totalCents();
    }

    public InvoiceTotal calculate(Invoice invoice) {
        guardNotEmpty(invoice);

        var summary = invoice.lines()
            .stream()
            .map(line -> switch (line) {
                case PhysicalProduct product -> {
                    guardPositive(product.quantity(), "Physical product quantity must be positive");
                    guardPositive(product.unitPriceCents(), "Physical product unit price must be positive");
                    yield new InvoiceLineSummary(
                        product.unitPriceCents() * product.quantity(),
                        product.quantity()
                    );
                }
                case OnlineProduct product -> {
                    guardPositive(product.licenseCount(), "Online product license count must be positive");
                    guardPositive(product.unitPriceCents(), "Online product unit price must be positive");
                    yield new InvoiceLineSummary(
                        product.unitPriceCents() * product.licenseCount(),
                        product.licenseCount()
                    );
                }
            })
            .reduce(
                new InvoiceLineSummary(0, 0),
                (left, right) -> new InvoiceLineSummary(
                    left.amountCents() + right.amountCents(),
                    left.productCount() + right.productCount()
                )
            );

        guardProductLimit(summary.productCount());

        var discountCents = discountCents(invoice.customerType(), summary.amountCents());

        return new InvoiceTotal(
            summary.amountCents(),
            discountCents,
            summary.amountCents() - discountCents
        );
    }

    private static void guardNotEmpty(Invoice invoice) {
        if (invoice.lines().isEmpty()) {
            throw new IllegalArgumentException("Invoice must contain at least one product");
        }
    }

    private static void guardProductLimit(int productCount) {
        if (productCount > MAX_PRODUCTS) {
            throw new IllegalArgumentException("Invoice cannot contain more than 10 products");
        }
    }

    private static int discountCents(CustomerType customerType, int subtotalCents) {
        DiscountRule rule = switch (customerType) {
            case STANDARD -> new StandardDiscount(subtotalCents);
            case PREMIUM -> new PremiumDiscount(subtotalCents);
            case VIP -> new VipDiscount(subtotalCents);
        };

        return switch (rule) {
            case StandardDiscount ignored -> 0;
            case PremiumDiscount premium when premium.subtotalCents() >= 10_000 -> pct(premium.subtotalCents(), 5);
            case PremiumDiscount ignored -> 0;
            case VipDiscount vip when vip.subtotalCents() >= 20_000 -> pct(vip.subtotalCents(), 10);
            case VipDiscount vip -> pct(vip.subtotalCents(), 5);
        };
    }

    private static int pct(int amountCents, int percent) {
        return amountCents * percent / 100;
    }

    private static void guardPositive(int value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    private sealed interface DiscountRule permits StandardDiscount, PremiumDiscount, VipDiscount {}

    private record StandardDiscount(int subtotalCents) implements DiscountRule {}

    private record PremiumDiscount(int subtotalCents) implements DiscountRule {}

    private record VipDiscount(int subtotalCents) implements DiscountRule {}

    private record InvoiceLineSummary(int amountCents, int productCount) {}
}
