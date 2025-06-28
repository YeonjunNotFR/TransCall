package com.youhajun.feature.call.impl.service

import androidx.compose.runtime.staticCompositionLocalOf
import com.youhajun.core.model.calling.CallingMessage
import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallMediaUser
import com.youhajun.webrtc.model.MediaContentType
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CallServiceContract {
    val mediaUsersFlow: StateFlow<List<CallMediaUser>>
    val messageFlow: SharedFlow<CallingMessage>

    fun flipCamera()
    fun setCameraEnabled(enabled: Boolean)

    fun setMicEnabled(enabled: Boolean)
    fun setMuteEnable(enabled: Boolean)
    fun setAudioDeviceChange(deviceType: AudioDeviceType)
    fun setOutputEnable(userId: String, mediaContentType: MediaContentType, enabled: Boolean)
    fun callingLeft()
}

val LocalCallServiceContract = staticCompositionLocalOf<CallServiceContract?> {
    error("No CallServiceContract provided")
}
