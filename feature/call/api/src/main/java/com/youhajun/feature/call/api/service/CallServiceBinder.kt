package com.youhajun.feature.call.api.service

import android.os.Binder

abstract class CallServiceBinder : Binder() {

    abstract fun getContract(): CallServiceContract

    abstract fun ongoingCallRoomId(): String?

    abstract fun onFinish(finish: () -> Unit)
}