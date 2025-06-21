package com.youhajun.webrtc.peer

import org.webrtc.AddIceObserver
import org.webrtc.IceCandidate
import org.webrtc.PeerConnection
import org.webrtc.WebRTCException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal suspend fun PeerConnection.addRtcIceCandidate(iceCandidate: IceCandidate): Result<Unit> {
  return suspendCoroutine { cont ->
    addIceCandidate(
      iceCandidate,
      object : AddIceObserver {
        override fun onAddSuccess() {
          cont.resume(Result.success(Unit))
        }

        override fun onAddFailure(error: String?) {
          cont.resume(Result.failure(WebRTCException(message = error)))
        }
      },
    )
  }
}
