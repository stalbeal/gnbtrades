package com.saba.gnbtrades.util

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.getFormattedAmount() = setScale(2, RoundingMode.HALF_EVEN)
