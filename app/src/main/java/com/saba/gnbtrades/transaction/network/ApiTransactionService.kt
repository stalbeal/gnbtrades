package com.saba.gnbtrades.transaction.network

import retrofit2.http.GET

interface ApiTransactionService {

    @GET("transactions")
    suspend fun getTransactions(): List<ApiTransactionResponse>

}
