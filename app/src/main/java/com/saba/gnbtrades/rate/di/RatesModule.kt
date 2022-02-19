package com.saba.gnbtrades.rate.di

import com.saba.gnbtrades.rate.network.ApiRateService
import com.saba.gnbtrades.rate.repository.RatesRepository
import com.saba.gnbtrades.rate.repository.RatesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RatesModule {

    @Singleton
    @Provides
    fun provideRatesRepository(ratesRepository: RatesRepositoryImpl): RatesRepository =
        ratesRepository

    @Reusable
    @Provides
    fun provideApiRateService(retrofit: Retrofit): ApiRateService =
        retrofit.create(ApiRateService::class.java)
}
