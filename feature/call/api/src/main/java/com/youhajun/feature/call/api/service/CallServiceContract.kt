package com.youhajun.feature.call.api.service

import androidx.compose.runtime.staticCompositionLocalOf
import com.youhajun.core.model.calling.ServerMessage
import com.youhajun.core.model.calling.type.MediaContentType
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallMediaUser
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CallServiceContract {
    val mediaUsersFlow: StateFlow<List<CallMediaUser>>
    val messageFlow: SharedFlow<ServerMessage>

    fun flipCamera()
    fun setCameraEnabled(enabled: Boolean)

    fun setMicEnabled(enabled: Boolean)
    fun setMuteEnable(enabled: Boolean)
    fun setAudioDeviceChange(deviceType: AudioDeviceType)
    fun setOutputEnable(userId: String, mediaContentType: MediaContentType, enabled: Boolean)
    fun leaveCall()
}

val LocalCallServiceContract = staticCompositionLocalOf<CallServiceContract?> {
    error("No CallServiceContract provided")
}

val LocalOngoingCallRoomId = staticCompositionLocalOf<String?> {
    null
}
