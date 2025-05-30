package com.youhajun.core.notification.di

import com.youhajun.core.notification.call.CallNotificationHandler
import com.youhajun.core.notification.call.CallNotificationHandlerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal interface NotificationModule {

    @Singleton
    @Binds
    fun bindCallNotificationHandler(
        impl: CallNotificationHandlerImpl
    ): CallNotificationHandler
}