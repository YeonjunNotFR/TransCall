package com.youhajun.hyanghae.graphics.modifier

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.LayoutDirection

fun Modifier.removeParentPadding(parentPaddingValue: PaddingValues): Modifier =
    this.then(Modifier.layout { measurable, constraints ->
        val parentHorizontalPadding = parentPaddingValue.calculateStartPadding(LayoutDirection.Ltr) + parentPaddingValue.calculateEndPadding(LayoutDirection.Ltr)
        val parentVerticalPadding = parentPaddingValue.calculateTopPadding() + parentPaddingValue.calculateBottomPadding()

        val placeable = measurable.measure(
            constraints.copy(
                maxWidth = constraints.maxWidth + parentHorizontalPadding.roundToPx(),
                maxHeight = constraints.maxHeight + parentVerticalPadding.roundToPx()
            )
        )
        layout(placeable.width, placeable.height) {
            placeable.place(0,0)
        }
    })