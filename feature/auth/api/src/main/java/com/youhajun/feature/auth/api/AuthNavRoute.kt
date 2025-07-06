package com.youhajun.feature.auth.api

import android.net.Uri
import com.youhajun.core.route.DeepLinkRoute
import com.youhajun.core.route.PathTemplate
import com.youhajun.core.route.TransCallRoute
import kotlinx.serialization.Serializable

sealed interface AuthNavRoute : TransCallRoute {
    @Serializable
    data object Login : AuthNavRoute, DeepLinkRoute {
        override val pathTemplate = PathTemplate("login")

        override fun uriToRoute(uri: Uri): TransCallRoute? {
            return if (pathTemplate.match(uri) != null) this else null
        }
    }
}