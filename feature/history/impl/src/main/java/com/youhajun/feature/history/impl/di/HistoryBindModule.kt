package com.youhajun.feature.history.impl.di

import com.youhajun.feature.history.api.HistoryNavGraphRegistrar
import com.youhajun.feature.history.impl.navigation.HistoryNavGraphRegistrarImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal abstract class HistoryBindModule {

    @Binds
    abstract fun historyNavGraphRegisterBind(
        impl: HistoryNavGraphRegistrarImpl,
    ): HistoryNavGraphRegistrar
}