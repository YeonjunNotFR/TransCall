package com.youhajun.transcall.core.ui.components.bottomSheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetComp(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    isClickableOutside: Boolean = false,
    isSkipPartiallyExpanded: Boolean = false,
    dragHandler: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    onVisibilityChange: (Boolean) -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = isSkipPartiallyExpanded,
        confirmValueChange = { isClickableOutside }
    )
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            showBottomSheet = true
            sheetState.show()
        } else {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                showBottomSheet = false
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onVisibilityChange(false) },
            sheetState = sheetState,
            dragHandle = dragHandler,
            modifier = modifier,
            shape = shape,
            containerColor = containerColor
        ) {
            content()

            Spacer(Modifier.navigationBarsPadding())
        }
    }
}