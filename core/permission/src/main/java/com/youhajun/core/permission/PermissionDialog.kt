package com.youhajun.core.permission

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Typography
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable

@Composable
fun PermissionDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                title,
                style = Typography.titleLarge,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                message,
                style = Typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Text(
                confirmText,
                style = Typography.labelLarge,
                modifier = Modifier.noRippleClickable { onConfirm() }
            )
        },
        dismissButton = {
            Text(
                dismissText,
                style = Typography.labelLarge,
                modifier = Modifier.noRippleClickable { onDismiss() }
            )
        },
        shape = RoundedCornerShape(16.dp)
    )
}
