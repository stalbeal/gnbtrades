package com.saba.gnbtrades.rate.network

import com.google.gson.annotations.SerializedName
import com.saba.gnbtrades.Currency

data class ApiRateResponse(
    @SerializedName("from") val from: Currency,
    @SerializedName("to") val to: Currency,
    @SerializedName("rate") val value: String
)
