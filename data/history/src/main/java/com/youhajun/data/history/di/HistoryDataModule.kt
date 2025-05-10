package com.youhajun.data.history.di

import com.youhajun.data.history.HistoryRemoteDataSource
import com.youhajun.data.history.HistoryRemoteDataSourceImpl
import com.youhajun.data.history.HistoryRepositoryImpl
import com.youhajun.domain.history.HistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal interface HistoryDataModule {

    @Singleton
    @Binds
    fun bindHistoryRemoteDataSource(
        remote: HistoryRemoteDataSourceImpl
    ): HistoryRemoteDataSource

    @Singleton
    @Binds
    fun bindHistoryRepository(
        repo: HistoryRepositoryImpl
    ): HistoryRepository
}