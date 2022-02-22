package com.saba.gnbtrades.transactiondetail.ui

import com.saba.gnbtrades.Currency
import com.saba.gnbtrades.rate.model.Rate
import com.saba.gnbtrades.rate.usecase.GetRatesUseCase
import com.saba.gnbtrades.transaction.model.Transaction
import com.saba.gnbtrades.util.getFormattedAmount
import javax.inject.Inject

class ConvertCurrencyTransactionsAmountToEURUseCase @Inject constructor(private val getRatesUseCase: GetRatesUseCase) {

    suspend fun execute(transactions: List<Transaction>): List<Transaction> {
        val rates = getRatesUseCase.execute()
        return transactions.map {
            if (it.currency != Currency.EUR) {
                convertPath(
                    transaction = it,
                    from = it.currency,
                    rates = rates
                )
            } else {
                it
            }

        }
    }

    private fun convertPath(
        transaction: Transaction,
        from: Currency,
        rates: Map<Currency, List<Rate>>
    ): Transaction {
        val fromDestinations = getDestinationsForCurrencyOrigin(from, rates)

        if (fromDestinations.contains(Currency.EUR)) {
            val destinationRate = rates[from]!!.first { it.to == Currency.EUR }

            return transaction.copy(
                amount = (transaction.amount * destinationRate.value).getFormattedAmount(),
                currency = Currency.EUR
            )
        }

        val result = willPathLeadMeToEur(from, rates, mutableListOf())
        if (result.isEmpty()) {
            throw IllegalStateException("There are not routes for conversion from $from to ${Currency.EUR}")
        }

        var amount = transaction.amount
        result.forEach {
            amount *= it.value
        }

        return transaction.copy(
            amount = amount.getFormattedAmount(),
            currency = Currency.EUR
        )

    }

    private fun willPathLeadMeToEur(
        from: Currency,
        rates: Map<Currency, List<Rate>>,
        x: MutableList<Rate>,
        previousCurrencyEvaluated: Currency? = null
    ): MutableList<Rate> {
        if (rates[from]!!.count { it.to == Currency.EUR } > 0) {
            return x.apply { add(rates[from]!!.first { it.to == Currency.EUR }) }
        }


        for (rate in rates[from]!!) {
            if (rate.to != previousCurrencyEvaluated && isWorth(rate.to, rates, from)) {
                x.add(rate)
                willPathLeadMeToEur(rate.to, rates, x, from)
            }
        }

        return x
    }

    private fun isWorth(
        from: Currency,
        rates: Map<Currency, List<Rate>>,
        previousCurrencyEvaluated: Currency? = null
    ): Boolean {
        val pathToVerify = rates[from] ?: return false

        if (pathToVerify.count { it.to == Currency.EUR } > 0) {
            return true
        }

        for (rate in pathToVerify) {
            if (rate.to != previousCurrencyEvaluated) {
                if (isWorth(rate.to, rates, from)) {
                    return true
                }
            }
        }

        return false
    }

    /***
     * AUD -> EUR
     *
     * AUD -> CAD, COP
     * CAD -> COP, USD
     * USD -> EUR, CAD
     * COP -> CAD
     */

    private fun getDestinationsForCurrencyOrigin(
        currency: Currency,
        rates: Map<Currency, List<Rate>>
    ): Set<Currency> {
        return rates[currency]!!.map {
            it.to
        }.toSet()
    }
}
