package com.youhajun.data.auth.di

import com.youhajun.core.network.TokenProvider
import com.youhajun.core.network.TokenRefresher
import com.youhajun.data.auth.AuthLocalDataSource
import com.youhajun.data.auth.AuthLocalDataSourceImpl
import com.youhajun.data.auth.AuthRemoteDataSource
import com.youhajun.data.auth.AuthRemoteDataSourceImpl
import com.youhajun.data.auth.AuthRepositoryImpl
import com.youhajun.data.auth.TokenRefresherImpl
import com.youhajun.domain.auth.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal interface AuthDataModule {

    @Singleton
    @Binds
    fun bindTokenProvider(
        provider: AuthLocalDataSourceImpl
    ): TokenProvider

    @Singleton
    @Binds
    fun bindTokenRefresher(
        impl: TokenRefresherImpl
    ): TokenRefresher

    @Singleton
    @Binds
    fun bindAuthLocalDataSource(
        local: AuthLocalDataSourceImpl
    ): AuthLocalDataSource

    @Singleton
    @Binds
    fun bindAuthRemoteDataSource(
        remote: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Singleton
    @Binds
    fun bindAuthRepository(
        repository: AuthRepositoryImpl
    ): AuthRepository
}