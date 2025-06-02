package com.youhajun.core.route

import android.net.Uri

interface DeepLinkRoute: TransCallRoute {
    val pathTemplate: PathTemplate
    fun uriToRoute(uri: Uri): TransCallRoute?
}