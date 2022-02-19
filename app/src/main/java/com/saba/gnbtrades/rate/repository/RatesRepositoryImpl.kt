package com.saba.gnbtrades.rate.repository

import com.saba.gnbtrades.rate.models.Rate
import com.saba.gnbtrades.rate.network.ApiRateService
import javax.inject.Inject

class RatesRepositoryImpl @Inject constructor(private val apiRateService: ApiRateService) : RatesRepository {

    override suspend fun getRates(): List<Rate> {
        return apiRateService.getRates().map {
            Rate(it.from, it.to, it.value);
        }
    }

}
