package com.saba.gnbtrades.rate.repository

import com.saba.gnbtrades.Currency
import com.saba.gnbtrades.rate.model.Rate
import com.saba.gnbtrades.rate.network.ApiRateService
import java.math.BigDecimal
import javax.inject.Inject

class RatesRepositoryImpl @Inject constructor(private val apiRateService: ApiRateService) : RatesRepository {

    override suspend fun getRates(): List<Rate> {
        return apiRateService.getRates().map {
            Rate(Currency.valueOf(it.from), Currency.valueOf(it.to), BigDecimal(it.value));
        }
    }

}
