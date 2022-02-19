package com.saba.gnbtrades.rate.network

import retrofit2.http.GET

interface ApiRateService {

    @GET("rates")
    suspend fun getRates(): List<com.saba.gnbtrades.rate.network.ApiRateResponse>

}
