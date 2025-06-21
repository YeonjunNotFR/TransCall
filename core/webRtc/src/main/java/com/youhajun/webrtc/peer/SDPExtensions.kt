package com.youhajun.webrtc.peer

import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal suspend inline fun createSessionDescription(
  crossinline call: (SdpObserver) -> Unit,
): Result<SessionDescription> = suspendCoroutine {
  val observer = object : SdpObserver {

    override fun onCreateSuccess(description: SessionDescription?) {
      if (description != null) {
        it.resume(Result.success(description))
      } else {
        it.resume(Result.failure(RuntimeException("SessionDescription is null!")))
      }
    }

    override fun onCreateFailure(message: String?) = it.resume(Result.failure(RuntimeException(message)))
    override fun onSetSuccess() = Unit
    override fun onSetFailure(p0: String?) = Unit
  }

  call(observer)
}

internal suspend inline fun suspendSdpObserver(
  crossinline call: (SdpObserver) -> Unit,
): Result<Unit> = suspendCoroutine {
  val observer = object : SdpObserver {

    override fun onSetSuccess() = it.resume(Result.success(Unit))
    override fun onSetFailure(message: String?) = it.resume(Result.failure(RuntimeException(message)))
    override fun onCreateFailure(p0: String?) = Unit
    override fun onCreateSuccess(p0: SessionDescription?) = Unit
  }

  call(observer)
}
