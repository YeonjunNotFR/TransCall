package com.youhajun.webrtc.session

import com.youhajun.webrtc.SignalingClient
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallMediaUser
import com.youhajun.webrtc.model.MicChunk
import com.youhajun.webrtc.model.TurnCredential
import com.youhajun.webrtc.model.VideoRoomHandleInfo
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface WebRtcSessionManager {

    val mediaUsersFlow: StateFlow<List<CallMediaUser>>
    val micFlow: SharedFlow<MicChunk>

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