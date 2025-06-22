package com.youhajun.webrtc.session

import com.youhajun.webrtc.SignalingClient
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallMediaUser
import com.youhajun.webrtc.model.MediaContentType
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.flow.StateFlow

interface WebRtcSessionManager {


    fun start(isCaller: Boolean)
    fun dispose()

    val mediaUsersFlow: StateFlow<List<CallMediaUser>>
    fun flipCamera()
    fun setCameraEnabled(enabled: Boolean)

    fun setMicEnabled(enabled: Boolean)
    fun setMuteEnable(enabled: Boolean)
    fun setOutputEnable(userId: String, mediaContentType: MediaContentType, enabled: Boolean)
    fun selectAudioDevice(deviceType: AudioDeviceType)

    @AssistedFactory
    interface Factory {
        fun create(signalingClient: SignalingClient, userId: String): WebRtcSessionManagerImpl
    }
}