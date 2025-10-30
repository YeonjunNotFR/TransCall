package com.youhajun.webrtc.client

import com.youhajun.webrtc.model.media.MediaStateMessageRequest
import com.youhajun.webrtc.model.media.MediaStateMessageResponse
import com.youhajun.webrtc.model.signaling.SignalingMessageRequest
import com.youhajun.webrtc.model.signaling.SignalingMessageResponse
import kotlinx.coroutines.flow.Flow

interface SignalingClient {
    suspend fun sendSignalingRequest(request: SignalingMessageRequest)
    suspend fun sendMediaStateRequest(request: MediaStateMessageRequest)
    fun observeSignalingResponse(): Flow<SignalingMessageResponse>
    fun observeMediaStateResponse(): Flow<MediaStateMessageResponse>
}