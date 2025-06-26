package com.youhajun.feature.history.api

import android.net.Uri
import com.youhajun.core.route.DeepLinkRoute
import com.youhajun.core.route.PathTemplate
import com.youhajun.core.route.TransCallRoute
import kotlinx.serialization.Serializable

sealed interface HistoryNavRoute : TransCallRoute {
    @Serializable
    data object HistoryList : HistoryNavRoute, DeepLinkRoute {
        override val pathTemplate = PathTemplate("history")

        override fun uriToRoute(uri: Uri): TransCallRoute? {
            return if (pathTemplate.match(uri) != null) this else null
        }
    }
}