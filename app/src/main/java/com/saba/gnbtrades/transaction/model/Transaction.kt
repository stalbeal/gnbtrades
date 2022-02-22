package com.saba.gnbtrades.transaction.model

import android.os.Parcelable
import com.saba.gnbtrades.Currency
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class Transaction(
    val sku: String,
    val amount: BigDecimal,
    val currency: Currency
) : Parcelable
