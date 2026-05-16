package com.edwyn.demo.visitor.kotlin.payment

import com.edwyn.demo.visitor.kotlin.customer.Customer
import com.edwyn.demo.visitor.kotlin.customer.Premium
import com.edwyn.demo.visitor.kotlin.customer.Standard
import com.edwyn.demo.visitor.kotlin.customer.Vip

object PaymentCalculator {
    fun calculate(totalCents: Int, customer: Customer, payment: PaymentMethod): PaymentResult =
        when (customer) {
            is Premium -> when (payment) {
                is CardPayment -> result(totalCents, 0, "PREMIUM x Carte ${payment.network} : gratuit")
                is CryptoPayment -> result(totalCents, pct(totalCents, 3), "PREMIUM x Crypto ${payment.currency} : cashback 3%")
            }

            is Vip -> when (payment) {
                is CardPayment -> result(totalCents, -pct(totalCents, 1), "VIP x Carte ${payment.network} : frais 1%")
                is CryptoPayment if totalCents > 20_000 ->
                    result(totalCents, pct(totalCents, 1), "VIP x Crypto ${payment.currency} (> 200 EUR) : cashback 1%")

                is CryptoPayment ->
                    result(totalCents, 0, "VIP x Crypto ${payment.currency} (<= 200 EUR) : neutre")
            }

            is Standard -> when (payment) {
                is CardPayment -> result(totalCents, -pct(totalCents, 2), "STANDARD x Carte ${payment.network} : frais 2%")
                is CryptoPayment -> result(totalCents, -pct(totalCents, 1), "STANDARD x Crypto ${payment.currency} : frais 1%")
            }
        }

    private fun pct(amount: Int, percent: Int): Int =
        amount * percent / 100

    private fun result(base: Int, adjustment: Int, description: String): PaymentResult =
        PaymentResult(base, adjustment, base + adjustment, description)
}
