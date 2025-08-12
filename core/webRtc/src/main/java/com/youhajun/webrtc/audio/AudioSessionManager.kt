package com.youhajun.webrtc.audio

import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallAudioStream
import com.youhajun.webrtc.model.LocalAudioEvent
import com.youhajun.webrtc.model.MediaContentType
import com.youhajun.webrtc.model.MediaMessage
import com.youhajun.webrtc.model.RemoteAudioStream
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.webrtc.AudioTrack

internal interface AudioSessionManager {
    val audioStreamsFlow: StateFlow<List<CallAudioStream>>
    val localAudioEvent: SharedFlow<LocalAudioEvent>
    fun startAudio(localUserId: String): AudioTrack
    fun dispose()
    fun setMicEnabled(localUserId: String, enabled: Boolean)
    fun setMuteEnable(enabled: Boolean)
    fun setOutputEnable(userId: String, mediaContentType: MediaContentType, enabled: Boolean)
    fun selectAudioDevice(deviceType: AudioDeviceType)
    fun setAudioStateChange(state: MediaMessage.AudioStateChange)
    fun addRemoteAudioTrack(remoteAudio: RemoteAudioStream)
}
