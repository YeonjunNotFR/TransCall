package com.youhajun.webrtc.audio

import com.youhajun.webrtc.model.AudioDeviceType
import com.youhajun.webrtc.model.CallAudioStream
import com.youhajun.webrtc.model.LocalAudioEvent
import com.youhajun.webrtc.model.MediaContentType
import com.youhajun.webrtc.model.MediaState
import com.youhajun.webrtc.model.RemoteAudioStream
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.webrtc.AudioTrack

internal interface AudioSessionManager {
    val audioStreamsFlow: StateFlow<List<CallAudioStream>>
    val localAudioEvent: SharedFlow<LocalAudioEvent>
    val myAudioPcmFlow: SharedFlow<ByteArray>
    fun startAudio(localUserId: String): AudioTrack
    fun dispose()
    fun setMicEnabled(localUserId: String, enabled: Boolean)
    fun setMuteEnable(enabled: Boolean)
    fun setOutputEnable(userId: String, mediaContentType: MediaContentType, enabled: Boolean)
    fun selectAudioDevice(deviceType: AudioDeviceType)
    fun addRemoteAudioTrack(remoteAudio: RemoteAudioStream)
    fun onMediaStateChanged(state: MediaState)
    fun onMediaStateInit(list: List<MediaState>)
}
