package com.saba.gnbtrades.transactiondetail.repository

import com.saba.gnbtrades.transactiondetail.model.Rate

interface RatesRepository {

    suspend fun getRates(): List<Rate>
}


