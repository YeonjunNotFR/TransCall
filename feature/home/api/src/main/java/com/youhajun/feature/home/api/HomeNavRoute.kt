package com.youhajun.feature.home.api

import android.net.Uri
import com.youhajun.core.route.DeepLinkRoute
import com.youhajun.core.route.PathTemplate
import com.youhajun.core.route.TransCallRoute
import kotlinx.serialization.Serializable

sealed interface HomeNavRoute : TransCallRoute {
    @Serializable
    data object Home : HomeNavRoute, DeepLinkRoute {
        override val pathTemplate = PathTemplate("home")

        override fun uriToRoute(uri: Uri): TransCallRoute? {
            return if (pathTemplate.match(uri) != null) this else null
        }
    }
}