package com.youhajun.feature.history.di

import com.youhajun.feature.history.api.ConversationSyncLauncher
import com.youhajun.feature.history.worker.ConversationSyncLauncherImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkerModule {

    @Binds
    abstract fun bindConversationSyncLauncher(
        impl: ConversationSyncLauncherImpl
    ): ConversationSyncLauncher
}