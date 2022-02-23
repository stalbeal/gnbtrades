package com.saba.gnbtrades.di

import com.saba.gnbtrades.network.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://quiet-stone-2094.herokuapp.com/"

    @Singleton
    @Provides
    fun providesOkHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(headerInterceptor).build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideHeaderInterceptor() = HeaderInterceptor()
}
