package com.youhajun.webrtc.session

import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallMediaUser
import com.youhajun.webrtc.model.TurnCredential
import com.youhajun.webrtc.model.VideoRoomHandleInfo
import kotlinx.coroutines.flow.StateFlow

interface WebRtcSessionManager {

    fun start(localUserId: String, videoRoomHandleInfo: VideoRoomHandleInfo)
    fun initConfig(localUserId: String, turnCredential: TurnCredential, signalingClient: SignalingClient)
    fun start(videoRoomHandleInfo: VideoRoomHandleInfo)
    fun dispose()

    val mediaUsersFlow: StateFlow<List<CallMediaUser>>
    fun flipCamera()
    fun setCameraEnabled(enabled: Boolean)

    fun setMicEnabled(enabled: Boolean)
    fun setMuteEnable(enabled: Boolean)
    fun setOutputEnable(userId: String, mediaContentType: String, enabled: Boolean)
    fun selectAudioDevice(deviceType: AudioDeviceType)
}