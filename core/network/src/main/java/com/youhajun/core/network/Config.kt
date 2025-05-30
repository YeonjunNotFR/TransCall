package com.youhajun.core.network

internal object Config {
    val REST_BASE_URL = if (BuildConfig.DEBUG) {
        "https://dev.api.transcall.com"
    } else {
        "https://api.transcall.com"
    }

    val WEBSOCKET_BASE_URL = if (BuildConfig.DEBUG) {
        "wss://dev.api.transcall.com"
    } else {
        "wss://api.transcall.com"
    }
}