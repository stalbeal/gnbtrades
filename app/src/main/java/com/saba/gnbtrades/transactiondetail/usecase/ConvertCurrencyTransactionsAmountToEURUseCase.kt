package com.saba.gnbtrades.transactiondetail.usecase

import com.saba.gnbtrades.Currency
import com.saba.gnbtrades.transaction.model.Transaction
import com.saba.gnbtrades.transactiondetail.model.Rate
import com.saba.gnbtrades.util.getFormattedAmount
import javax.inject.Inject

class ConvertCurrencyTransactionsAmountToEURUseCase @Inject constructor(private val getRatesUseCase: GetRatesUseCase) {

    suspend fun execute(transactions: List<Transaction>): List<Transaction> {
        val rates = getRatesUseCase.execute()
        return transactions.map {
            if (it.currency != Currency.EUR) {
                convertTransactionAmountToEURCurrency(
                    transaction = it,
                    fromCurrency = it.currency,
                    rates = rates
                )
            } else {
                it
            }

        }
    }

    private fun convertTransactionAmountToEURCurrency(
        transaction: Transaction,
        fromCurrency: Currency,
        rates: Map<Currency, List<Rate>>
    ): Transaction {
        val fromCurrencyDestinations = getDestinationsForCurrencyOrigin(fromCurrency, rates)

        if (fromCurrencyDestinations.contains(Currency.EUR)) {
            val destinationRate = rates[fromCurrency]!!.first { it.to == Currency.EUR }

            return transaction.copy(
                amount = (transaction.amount * destinationRate.value).getFormattedAmount(),
                currency = Currency.EUR
            )
        }

        val result = willPathLeadMeToEur(fromCurrency, rates, mutableListOf())

        if (result.isEmpty()) {
            throw IllegalStateException("There are not routes for conversion from $fromCurrency to ${Currency.EUR}")
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
        fromCurrency: Currency,
        rates: Map<Currency, List<Rate>>,
        validRatesToConversion: MutableList<Rate>,
        previousCurrencyEvaluated: Currency? = null
    ): MutableList<Rate> {
        if (rates[fromCurrency]!!.count { it.to == Currency.EUR } > 0) {
            return validRatesToConversion.apply { add(rates[fromCurrency]!!.first { it.to == Currency.EUR }) }
        }


        for (rate in rates[fromCurrency]!!) {
            if (rate.to != previousCurrencyEvaluated && isWorth(rate.to, rates, fromCurrency)) {
                validRatesToConversion.add(rate)
                willPathLeadMeToEur(rate.to, rates, validRatesToConversion, fromCurrency)
            }
        }

        return validRatesToConversion
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

    private fun getDestinationsForCurrencyOrigin(
        currency: Currency,
        rates: Map<Currency, List<Rate>>
    ): Set<Currency> {
        return rates[currency]!!.map {
            it.to
        }.toSet()
    }
}
