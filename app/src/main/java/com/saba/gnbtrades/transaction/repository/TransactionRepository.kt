package com.saba.gnbtrades.transaction.repository

import com.saba.gnbtrades.transaction.model.Transaction

interface TransactionRepository {

    suspend fun getTransactions(): List<Transaction>
}

