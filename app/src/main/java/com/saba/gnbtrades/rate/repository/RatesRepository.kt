package com.saba.gnbtrades.rate.repository

import com.saba.gnbtrades.rate.model.Rate

interface RatesRepository {

    suspend fun getRates(): List<Rate>
}


