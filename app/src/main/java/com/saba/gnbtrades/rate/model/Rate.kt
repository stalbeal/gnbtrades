package com.saba.gnbtrades.rate.model

import com.saba.gnbtrades.Currency
import java.math.BigDecimal

data class Rate(
    val from: Currency,
    val to: Currency,
    val value: BigDecimal
)
