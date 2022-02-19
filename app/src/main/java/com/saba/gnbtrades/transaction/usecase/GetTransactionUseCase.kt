package com.saba.gnbtrades.transaction.usecase

import com.saba.gnbtrades.transaction.model.Transaction
import com.saba.gnbtrades.transaction.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {

    suspend fun execute(): List<Transaction> = transactionRepository.getTransactions()
}
