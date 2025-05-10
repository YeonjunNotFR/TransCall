package com.youhajun.data.history

sealed class HistoryEndpoint(val path: String) {
    data object List : HistoryEndpoint("/histories")
    data class Detail(val callId: String) : HistoryEndpoint("/histories/$callId")
    data class Delete(val callId: String) : HistoryEndpoint("/histories/$callId")
}