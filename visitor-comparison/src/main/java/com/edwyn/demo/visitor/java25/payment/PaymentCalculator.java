package com.edwyn.demo.visitor.java25.payment;

import com.edwyn.demo.visitor.java25.customer.Customer;
import com.edwyn.demo.visitor.java25.customer.Premium;
import com.edwyn.demo.visitor.java25.customer.Standard;
import com.edwyn.demo.visitor.java25.customer.Vip;

public final class PaymentCalculator {

    private PaymentCalculator() {}

    public static PaymentResult calculate(int totalCents, Customer customer, PaymentMethod payment) {
        return switch (customer) {
            case Premium p -> switch (payment) {
                case CardPayment c    -> result(totalCents, 0,                    "PREMIUM x Carte " + c.network() + " : gratuit");
                case CryptoPayment cr -> result(totalCents,  pct(totalCents, 3),  "PREMIUM x Crypto " + cr.currency() + " : cashback 3%");
            };
            case Vip v -> switch (payment) {
                case CardPayment c                              -> result(totalCents, -pct(totalCents, 1), "VIP x Carte " + c.network() + " : frais 1%");
                case CryptoPayment cr when totalCents > 20_000  -> result(totalCents,  pct(totalCents, 1), "VIP x Crypto " + cr.currency() + " (> 200 EUR) : cashback 1%");
                case CryptoPayment cr                           -> result(totalCents, 0,                   "VIP x Crypto " + cr.currency() + " (<= 200 EUR) : neutre");
            };
            case Standard s -> switch (payment) {
                case CardPayment c    -> result(totalCents, -pct(totalCents, 2), "STANDARD x Carte " + c.network() + " : frais 2%");
                case CryptoPayment cr -> result(totalCents, -pct(totalCents, 1), "STANDARD x Crypto " + cr.currency() + " : frais 1%");
            };
        };
    }

    private static int pct(int amount, int percent) {
        return amount * percent / 100;
    }

    private static PaymentResult result(int base, int adjustment, String description) {
        return new PaymentResult(base, adjustment, base + adjustment, description);
    }
}
