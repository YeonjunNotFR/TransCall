package com.youhajun.webrtc

import com.youhajun.webrtc.model.SignalingMessageRequest
import com.youhajun.webrtc.model.SignalingMessageResponse
import kotlinx.coroutines.flow.Flow

interface SignalingClient {
    suspend fun sendSignalingRequest(request: SignalingMessageRequest)
    fun observeSignalingResponse(): Flow<SignalingMessageResponse>
}
