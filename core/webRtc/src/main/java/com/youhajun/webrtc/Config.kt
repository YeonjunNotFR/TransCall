package com.youhajun.webrtc

import com.youhajun.webrtc.model.local.MediaContentType
import com.youhajun.webrtc.model.local.TrackType

internal object Config {
    private const val SEPARATOR = "_"

    fun getVideoTrackId(mediaContentType: MediaContentType): String {
        return listOf(TrackType.VIDEO.type, mediaContentType.name).joinToString(SEPARATOR)
    }

    fun getAudioTrackId(mediaContentType: MediaContentType): String {
        return listOf(TrackType.AUDIO.type, mediaContentType.name).joinToString(SEPARATOR)
    }
}