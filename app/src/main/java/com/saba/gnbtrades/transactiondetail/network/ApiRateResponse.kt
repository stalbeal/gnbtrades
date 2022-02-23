package com.saba.gnbtrades.transactiondetail.network

import com.google.gson.annotations.SerializedName

data class ApiRateResponse(
    @SerializedName("from") val from: String,
    @SerializedName("to") val to: String,
    @SerializedName("rate") val value: String
)
