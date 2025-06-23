package com.youhajun.webrtc.di

import android.content.Context
import com.youhajun.webrtc.peer.StreamPeerConnectionFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.webrtc.EglBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CoreWebRtcModule {

    @Provides
    @Singleton
    fun provideStreamPeerConnectionFactory(
        @ApplicationContext context: Context,
        eglBaseContext: EglBase.Context
    ): StreamPeerConnectionFactory = StreamPeerConnectionFactory(context, eglBaseContext)

    @Provides
    @Singleton
    fun provideEglBase(): EglBase = EglBase.create()

    @Provides
    fun provideEglBaseContext(eglBase: EglBase): EglBase.Context = eglBase.eglBaseContext
}