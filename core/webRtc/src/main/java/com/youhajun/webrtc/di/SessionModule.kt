package com.youhajun.webrtc.di

import com.youhajun.webrtc.audio.AudioDeviceController
import com.youhajun.webrtc.audio.AudioDeviceControllerImpl
import com.youhajun.webrtc.audio.AudioSessionManager
import com.youhajun.webrtc.audio.AudioSessionManagerImpl
import com.youhajun.webrtc.audio.AudioStreamStore
import com.youhajun.webrtc.audio.AudioStreamStoreImpl
import com.youhajun.webrtc.session.WebRtcSessionManager
import com.youhajun.webrtc.session.WebRtcSessionManagerImpl
import com.youhajun.webrtc.video.CameraController
import com.youhajun.webrtc.video.CameraControllerImpl
import com.youhajun.webrtc.video.VideoSessionManager
import com.youhajun.webrtc.video.VideoSessionManagerImpl
import com.youhajun.webrtc.video.VideoStreamStore
import com.youhajun.webrtc.video.VideoStreamStoreImpl
import dagger.Binds
import dagger.Module
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

    @Binds
    @ServiceScoped
    abstract fun bindAudioSessionManager(
        impl: AudioSessionManagerImpl,
    ): AudioSessionManager

    @Binds
    @ServiceScoped
    abstract fun bindVideoSessionManager(
        impl: VideoSessionManagerImpl,
    ): VideoSessionManager

    @Binds
    @ServiceScoped
    abstract fun bindWebRtcSessionManager(
        impl: WebRtcSessionManagerImpl,
    ): WebRtcSessionManager
}
