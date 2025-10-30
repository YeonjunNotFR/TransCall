package com.youhajun.webrtc.session

import com.youhajun.webrtc.client.SignalingClient
import com.youhajun.webrtc.model.stream.AudioDeviceType
import com.youhajun.webrtc.model.stream.CallMediaUser
import com.youhajun.webrtc.model.signaling.TurnCredential
import com.youhajun.webrtc.model.signaling.VideoRoomHandleInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WebRtcSessionManager {

    val mediaUsersFlow: StateFlow<List<CallMediaUser>>
    val micByteFlow: Flow<ByteArray>

    fun initConfig(localUserId: String, turnCredential: TurnCredential, signalingClient: SignalingClient)
    fun start(videoRoomHandleInfo: VideoRoomHandleInfo)
    fun dispose()

    fun flipCamera()
    fun setCameraEnabled(enabled: Boolean)

    fun setMicEnabled(enabled: Boolean)
    fun setMuteEnable(enabled: Boolean)
    fun setOutputEnable(userId: String, mediaContentType: String, enabled: Boolean)
    fun selectAudioDevice(deviceType: AudioDeviceType)
}