package com.youhajun.data.room.di

import com.youhajun.data.room.RoomRemoteDataSource
import com.youhajun.data.room.RoomRemoteDataSourceImpl
import com.youhajun.data.room.RoomRepositoryImpl
import com.youhajun.domain.room.RoomRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal interface RoomDataModule {

    @Singleton
    @Binds
    fun bindRoomRemoteDataSource(
        remote: RoomRemoteDataSourceImpl
    ): RoomRemoteDataSource

    @Singleton
    @Binds
    fun bindRoomRepository(
        repo: RoomRepositoryImpl
    ): RoomRepository
}