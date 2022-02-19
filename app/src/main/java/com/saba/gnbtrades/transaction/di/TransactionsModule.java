package com.saba.gnbtrades.transaction.di;

import com.saba.gnbtrades.transaction.network.ApiTransactionService;
import com.saba.gnbtrades.transaction.repository.TransactionRepository;
import com.saba.gnbtrades.transaction.repository.TransactionRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class TransactionsModule {

    @Provides
    @Singleton
    public static TransactionRepository provideTransactionRepository(TransactionRepositoryImpl transactionRepository) {
        return transactionRepository;
    }

    @Provides
    @Reusable
    public static ApiTransactionService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiTransactionService.class);
    }
}
