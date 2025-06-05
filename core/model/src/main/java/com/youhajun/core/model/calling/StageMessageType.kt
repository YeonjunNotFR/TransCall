package com.youhajun.core.model.calling

sealed interface StageMessageType : CallingMessageType {
    data class Error(override val message: String) : StageMessageType, Throwable()

    data object Waiting : StageMessageType

    data class Signaling(val isCaller: Boolean) : StageMessageType

    data object Calling : StageMessageType

    data object Ended : StageMessageType
}