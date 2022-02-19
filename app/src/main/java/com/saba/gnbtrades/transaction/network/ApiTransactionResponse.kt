package com.saba.gnbtrades.transaction.network

import com.google.gson.annotations.SerializedName
import com.saba.gnbtrades.Currency

data class ApiTransactionResponse(
    @SerializedName("sku") val sku: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("currency") val currency: Currency
)
