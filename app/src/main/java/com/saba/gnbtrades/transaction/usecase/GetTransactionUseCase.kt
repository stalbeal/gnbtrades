package com.saba.gnbtrades.transaction.usecase

import com.saba.gnbtrades.transaction.model.Transaction
import com.saba.gnbtrades.transaction.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {

    suspend fun execute(): Map<String, List<Transaction>> {
        return groupTransactionsBySku(transactionRepository.getTransactions())
    }

    private fun groupTransactionsBySku(transactions: List<Transaction>): Map<String, List<Transaction>> {

        val transactionsBySku = HashMap<String, List<Transaction>>()

        transactions.forEach {

            if (!transactionsBySku.containsKey(it.sku)) {
                transactionsBySku[it.sku] = listOf(it)
            } else {
                transactionsBySku[it.sku] = transactionsBySku[it.sku]!!.toMutableList().apply {
                    add(it)
                }
            }
        }
        return transactionsBySku
    }

}
