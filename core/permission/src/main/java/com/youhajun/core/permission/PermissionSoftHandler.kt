package com.youhajun.core.permission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.youhajun.core.design.R
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PermissionSoftHandler(
    controller: PermissionRequestController,
    permissions: ImmutableList<String>,
    rationaleMessage: String? = null,
    askOnceKey: String = "soft:${permissions.joinToString()}",
    autoLaunch: Boolean = false,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: (() -> Unit) = {}
) {
    val context = LocalContext.current
    var hasAskedOnce by rememberSaveable(askOnceKey) { mutableStateOf(false) }
    var showSoftAskDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val allGranted = results.values.all { it }
        hasAskedOnce = true
        if (allGranted) onPermissionGranted() else onPermissionDenied()
    }

    LaunchedEffect(controller.triggerRequest) {
        if (!controller.triggerRequest) return@LaunchedEffect

        if (PermissionManager.checkPermissions(context, permissions)) {
            onPermissionGranted()
        } else {
            when {
                hasAskedOnce -> onPermissionDenied()

                autoLaunch -> {
                    hasAskedOnce = true
                    permissionLauncher.launch(permissions.toTypedArray())
                }

                else -> showSoftAskDialog = true
            }
        }
        controller.consume()
    }

    if (!autoLaunch && showSoftAskDialog) {
        val message = rationaleMessage ?: stringResource(R.string.permission_rationale_message_default)
        PermissionDialog(
            title = stringResource(R.string.permission_rationale_title),
            message = message,
            confirmText = stringResource(R.string.permission_rationale_confirm),
            dismissText = stringResource(R.string.permission_rationale_dismiss),
            onConfirm = {
                showSoftAskDialog = false
                hasAskedOnce = true
                permissionLauncher.launch(permissions.toTypedArray())
            },
            onDismiss = {
                showSoftAskDialog = false
                hasAskedOnce = true
                onPermissionDenied()
            }
        )
    }
}
