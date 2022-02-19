package com.saba.gnbtrades.rate.repository

import com.saba.gnbtrades.rate.models.Rate

interface RatesRepository {

    suspend fun getRates(): List<Rate>
}


