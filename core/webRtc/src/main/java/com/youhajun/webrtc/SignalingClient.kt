package com.youhajun.webrtc

import com.youhajun.webrtc.model.MediaStateMessageRequest
import com.youhajun.webrtc.model.MediaStateMessageResponse
import com.youhajun.webrtc.model.SignalingMessageRequest
import com.youhajun.webrtc.model.SignalingMessageResponse
import kotlinx.coroutines.flow.Flow

interface SignalingClient {
    suspend fun sendSignalingRequest(request: SignalingMessageRequest)
    suspend fun sendMediaStateRequest(request: MediaStateMessageRequest)
    fun observeSignalingResponse(): Flow<SignalingMessageResponse>
    fun observeMediaStateResponse(): Flow<MediaStateMessageResponse>
}
