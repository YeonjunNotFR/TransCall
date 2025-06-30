package com.youhajun.feature.call.impl.di

import com.youhajun.feature.call.api.CallIntentFactory
import com.youhajun.feature.call.impl.CallIntentFactoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CallIntentFactoryModule {

    @Provides
    @Singleton
    fun provideCallIntentFactory(): CallIntentFactory {
        return CallIntentFactoryImpl()
    }
}