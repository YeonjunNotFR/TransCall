package com.youhajun.core.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class PermissionRequestController {
    internal var triggerRequest by mutableStateOf(false)
        private set

    fun request() {
        triggerRequest = true
    }

    internal fun consume() {
        triggerRequest = false
    }
}

@Composable
fun rememberPermissionRequestController(): PermissionRequestController {
    return remember { PermissionRequestController() }
}