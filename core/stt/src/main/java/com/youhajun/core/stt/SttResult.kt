package com.youhajun.core.stt

sealed class SttResult {
    data class Partial(val text: String) : SttResult()
    data class Final(val text: String) : SttResult()
    data class Error(val code: Int) : SttResult()
    data object Idle : SttResult()
}