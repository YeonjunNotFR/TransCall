package com.youhajun.feature.call.impl.di

import com.youhajun.feature.call.api.CallNavGraphRegistrar
import com.youhajun.feature.call.impl.navigation.CallNavGraphRegistrarImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal abstract class CallBindModule {

    @Binds
    abstract fun callNavGraphRegisterBind(
        graph: CallNavGraphRegistrarImpl,
    ): CallNavGraphRegistrar
}