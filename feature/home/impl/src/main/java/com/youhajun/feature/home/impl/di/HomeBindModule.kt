package com.youhajun.feature.home.impl.di

import com.youhajun.feature.home.api.HomeNavGraphRegistrar
import com.youhajun.feature.home.impl.navigation.HomeNavGraphRegistrarImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal abstract class HomeBindModule {

    @Binds
    abstract fun homeNavGraphRegisterBind(
        impl: HomeNavGraphRegistrarImpl,
    ): HomeNavGraphRegistrar
}