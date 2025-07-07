package com.youhajun.feature.auth.impl.di

import com.youhajun.feature.auth.api.GoogleAuthManager
import com.youhajun.feature.auth.impl.util.GoogleAuthManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal abstract class GoogleAuthModule {

    @Binds
    abstract fun bindGoogleAuthManager(
        impl: GoogleAuthManagerImpl,
    ): GoogleAuthManager
}