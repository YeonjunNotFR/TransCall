package com.youhajun.core.route

import android.net.Uri

object AppLink {

    private const val SCHEME = "app"
    private const val HOST = "transcall.com"
    internal const val BASE = "$SCHEME://$HOST"

    internal fun build(
        vararg segments: String,
        query: Map<String, String> = emptyMap()
    ): Uri = Uri.Builder()
        .scheme(SCHEME)
        .authority(HOST)
        .apply {
            segments.forEach { appendPath(it) }
            query.forEach { (k, v) -> appendQueryParameter(k, v) }
        }
        .build()
}