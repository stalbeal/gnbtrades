package com.saba.gnbtrades.rate.models

import com.saba.gnbtrades.Currency

data class Rate(
    val from: Currency,
    val to: Currency,
    val value: String
)
