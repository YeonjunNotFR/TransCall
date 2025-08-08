package com.youhajun.feature.call.impl.di

import com.youhajun.feature.call.api.CallIntentFactory
import com.youhajun.feature.call.impl.CallIntentFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CallIntentFactoryModule {

    @Binds
    @Singleton
    abstract fun bindCallIntentFactory(
        callIntentFactoryImpl: CallIntentFactoryImpl
    ): CallIntentFactory
}