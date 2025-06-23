package com.youhajun.webrtc.di

import com.youhajun.webrtc.audio.AudioDeviceController
import com.youhajun.webrtc.audio.AudioDeviceControllerImpl
import com.youhajun.webrtc.audio.AudioStreamStore
import com.youhajun.webrtc.audio.AudioStreamStoreImpl
import com.youhajun.webrtc.video.CameraController
import com.youhajun.webrtc.video.CameraControllerImpl
import com.youhajun.webrtc.video.VideoStreamStore
import com.youhajun.webrtc.video.VideoStreamStoreImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
internal abstract class SessionModule {

    @Binds
    @ServiceScoped
    abstract fun bindAudioDeviceController(
        impl: AudioDeviceControllerImpl,
    ): AudioDeviceController

    @Binds
    @ServiceScoped
    abstract fun bindCameraController(
        impl: CameraControllerImpl,
    ): CameraController

    @Binds
    @ServiceScoped
    abstract fun bindVideoStreamStore(
        impl: VideoStreamStoreImpl,
    ): VideoStreamStore

    @Binds
    @ServiceScoped
    abstract fun bindAudioStreamStore(
        impl: AudioStreamStoreImpl,
    ): AudioStreamStore
}
