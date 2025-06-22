package com.youhajun.webrtc

import com.youhajun.webrtc.model.MediaContentType
import com.youhajun.webrtc.model.TrackType

internal object Config {
    private const val STREAM_PREFIX = "stream"
    private const val SEPARATOR = "_"

    fun getVideoTrackId(userId: String, mediaContentType: MediaContentType): String {
        return listOf(TrackType.VIDEO.type, userId, mediaContentType.name).joinToString(SEPARATOR)
    }

    fun getAudioTrackId(userId: String, mediaContentType: MediaContentType): String {
        return listOf(TrackType.AUDIO.type, userId, mediaContentType.name).joinToString(SEPARATOR)
    }

    fun getStreamId(userId: String): String {
        return listOf(STREAM_PREFIX, userId).joinToString(SEPARATOR)
    }

    fun getParsedTrackId(trackId: String): List<String> {
        return trackId.split(SEPARATOR)
    }
}