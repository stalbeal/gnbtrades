package com.saba.gnbtrades.transaction.repository

import com.saba.gnbtrades.Currency
import com.saba.gnbtrades.transaction.model.Transaction
import com.saba.gnbtrades.transaction.network.ApiTransactionService
import java.math.BigDecimal
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(private val apiTransactionService: ApiTransactionService) :
    TransactionRepository {

    override suspend fun getTransactions(): List<Transaction> {
        return apiTransactionService.getTransactions()
            .map { Transaction(it.sku, BigDecimal(it.amount), Currency.valueOf(it.currency)) }
    }
}
