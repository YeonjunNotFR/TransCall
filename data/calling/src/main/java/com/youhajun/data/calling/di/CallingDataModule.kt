package com.youhajun.data.calling.di

import com.youhajun.data.calling.CallingDataSource
import com.youhajun.data.calling.CallingDataSourceImpl
import com.youhajun.data.calling.CallingRepositoryImpl
import com.youhajun.domain.calling.CallingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal interface CallingDataModule {

    @Singleton
    @Binds
    fun bindCallingDataSource(
        remote: CallingDataSourceImpl
    ): CallingDataSource

    @Singleton
    @Binds
    fun bindCallingRepository(
        repo: CallingRepositoryImpl
    ): CallingRepository
}