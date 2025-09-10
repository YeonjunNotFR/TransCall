package com.youhajun.core.permission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.youhajun.core.design.R
import com.youhajun.transcall.core.common.getActivity
import kotlinx.collections.immutable.ImmutableList
@Composable
fun PermissionForceHandler(
    controller: PermissionRequestController,
    permissions: ImmutableList<String>,
    rationaleMessage: String? = null,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: (() -> Unit) = {}
) {
    val context = LocalContext.current
    val activity = context.getActivity()
    var showRationale by remember { mutableStateOf(false) }
    var showDeniedDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val allGranted = results.values.all { it }
        val needsRationale = activity != null && PermissionManager.shouldShowRationale(activity, permissions)

        when {
            allGranted -> onPermissionGranted()
            needsRationale -> {
                showRationale = true
            }

            else -> {
                showDeniedDialog = true
            }
        }
    }

    LaunchedEffect(controller.triggerRequest) {
        if(!controller.triggerRequest) return@LaunchedEffect

        if (PermissionManager.checkPermissions(context, permissions)) {
            onPermissionGranted()
        } else {
            permissionLauncher.launch(permissions.toTypedArray())
        }
        controller.consume()
    }

    if (showRationale) {
        val message = rationaleMessage ?: stringResource(R.string.permission_rationale_message_default)
        PermissionDialog(
            title = stringResource(R.string.permission_rationale_title),
            message = message,
            confirmText = stringResource(R.string.permission_rationale_confirm),
            dismissText = stringResource(R.string.permission_rationale_dismiss),
            onConfirm = {
                showRationale = false
                permissionLauncher.launch(permissions.toTypedArray())
            },
            onDismiss = {
                showRationale = false
                onPermissionDenied()
            }
        )
    }

    if (showDeniedDialog) {
        PermissionDialog(
            title = stringResource(R.string.permission_denied_title),
            message = stringResource(R.string.permission_denied_message),
            confirmText = stringResource(R.string.permission_denied_confirm),
            dismissText = stringResource(R.string.permission_denied_dismiss),
            onConfirm = {
                showDeniedDialog = false
                PermissionManager.openAppSettings(context)
                onPermissionDenied()
            },
            onDismiss = {
                showDeniedDialog = false
                onPermissionDenied()
            }
        )
    }
}