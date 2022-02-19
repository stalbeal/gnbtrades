package com.saba.gnbtrades.transaction.model

import com.saba.gnbtrades.Currency

data class Transaction(
    val sku: String,
    val amount: String,
    val currency: Currency
)
