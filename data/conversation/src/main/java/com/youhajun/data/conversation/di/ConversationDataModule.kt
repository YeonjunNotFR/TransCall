package com.youhajun.data.conversation.di

import com.youhajun.data.conversation.ConversationLocalDataSource
import com.youhajun.data.conversation.ConversationLocalDataSourceImpl
import com.youhajun.data.conversation.ConversationRemoteDataSource
import com.youhajun.data.conversation.ConversationRemoteDataSourceImpl
import com.youhajun.data.conversation.ConversationRepositoryImpl
import com.youhajun.domain.conversation.ConversationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal interface ConversationDataModule {

    @Singleton
    @Binds
    fun bindConversationLocalDataSource(
        local: ConversationLocalDataSourceImpl
    ): ConversationLocalDataSource

    @Singleton
    @Binds
    fun bindConversationRemoteDataSource(
        remote: ConversationRemoteDataSourceImpl
    ): ConversationRemoteDataSource


    @Singleton
    @Binds
    fun bindConversationRepository(
        repo: ConversationRepositoryImpl
    ): ConversationRepository
}