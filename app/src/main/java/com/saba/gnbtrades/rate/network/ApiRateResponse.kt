package com.saba.gnbtrades.rate.network

import com.google.gson.annotations.SerializedName
import com.saba.gnbtrades.Currency

data class ApiRateResponse(
    @SerializedName("from") val from: String,
    @SerializedName("to") val to: String,
    @SerializedName("rate") val value: String
)
