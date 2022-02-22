package com.saba.gnbtrades.di

import com.saba.gnbtrades.util.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers =
        CoroutineDispatchers(Dispatchers.Main, Dispatchers.IO)
}
