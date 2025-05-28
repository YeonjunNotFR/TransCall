package com.youhajun.data.user.di

import com.youhajun.data.user.UserRemoteDataSource
import com.youhajun.data.user.UserRemoteDataSourceImpl
import com.youhajun.data.user.UserRepositoryImpl
import com.youhajun.domain.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface UserDataModule {

    @Singleton
    @Binds
    fun bindUserRemoteDataSource(
        remote: UserRemoteDataSourceImpl
    ): UserRemoteDataSource

    @Singleton
    @Binds
    fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}