package com.youhajun.core.route

import android.net.Uri

sealed interface DeepLinkEntry {
    val path: String
    val pattern: String

    fun toUri(query: Map<String, String> = emptyMap()): Uri

    data object HistoryList : DeepLinkEntry {
        override val path: String = "history"
        override val pattern: String = "${AppLink.BASE}/$path"
        override fun toUri(query: Map<String, String>): Uri = AppLink.build(path, query = query)
    }

    data class HistoryDetail(
        val historyId: String
    ) : DeepLinkEntry {
        override val path: String = PATH
        override val pattern: String = PATTERN
        override fun toUri(query: Map<String, String>): Uri = AppLink.build("history", historyId, query = query)

        companion object {
            const val PATH: String = "history/{historyId}"
            const val PATTERN: String = "${AppLink.BASE}/$PATH"
        }
    }

    data object RoomList : DeepLinkEntry {
        override val path: String = "room"
        override val pattern: String = "${AppLink.BASE}/$path"
        override fun toUri(query: Map<String, String>): Uri = AppLink.build(path, query = query)
    }

    data object CreateRoom : DeepLinkEntry {
        override val path: String = "create-room"
        override val pattern: String = "${AppLink.BASE}/$path"
        override fun toUri(query: Map<String, String>): Uri = AppLink.build(path, query = query)
    }

    data object Profile : DeepLinkEntry {
        override val path: String = "profile"
        override val pattern: String = "${AppLink.BASE}/$path"
        override fun toUri(query: Map<String, String>): Uri = AppLink.build(path, query = query)
    }

    data object Home : DeepLinkEntry {
        override val path: String = "home"
        override val pattern: String = "${AppLink.BASE}/$path"
        override fun toUri(query: Map<String, String>): Uri = AppLink.build(path, query = query)
    }
}