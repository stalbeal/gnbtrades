package com.saba.gnbtrades.transactiondetail.network

import retrofit2.http.GET

interface ApiRateService {

    @GET("rates")
    suspend fun getRates(): List<ApiRateResponse>

}
