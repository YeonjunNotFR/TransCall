package com.youhajun.feature.call.impl.di

import android.app.Service
import com.youhajun.feature.call.impl.service.CallForegroundService
import com.youhajun.webrtc.SignalingClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @ServiceScoped
    fun provideSignalingClient(
        service: Service
    ): SignalingClient {
        val callService = service as CallForegroundService
        return callService.webRtcSignalingClient
    }
}