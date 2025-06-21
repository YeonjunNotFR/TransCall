package com.youhajun.webrtc

import com.youhajun.webrtc.model.MediaMessage
import com.youhajun.webrtc.model.SignalingMessage
import kotlinx.coroutines.flow.Flow

interface SignalingClient {
    fun sendOffer(offer: SignalingMessage.Offer)
    fun sendAnswer(answer: SignalingMessage.Answer)
    fun sendIceCandidate(candidate: SignalingMessage.IceCandidate)
    fun observeSignalingMsg(): Flow<SignalingMessage>
    fun observeMediaMsg(): Flow<MediaMessage>
}
