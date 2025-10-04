package com.youhajun.data.history

sealed class HistoryEndpoint(val path: String) {
    data object List : HistoryEndpoint("api/call/histories")
    data class Detail(val historyId: String) : HistoryEndpoint("api/call/history/$historyId")
    data class Delete(val historyId: String) : HistoryEndpoint("api/call/history/$historyId")
}