package com.youhajun.transcall.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors

@Composable
fun LazyBackgroundColumn(
    modifier: Modifier = Modifier,
    isFirst: Boolean,
    isLast: Boolean,
    paddingValues: PaddingValues,
    radius: Dp = 12.dp,
    color: Color = Colors.White,
    content: @Composable () -> Unit,
) {
    val shape = when {
        isFirst && isLast -> RoundedCornerShape(radius)
        isFirst -> RoundedCornerShape(topStart = radius, topEnd = radius)
        isLast -> RoundedCornerShape(bottomStart = radius, bottomEnd = radius)
        else -> RoundedCornerShape(0.dp)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color, shape)
            .padding(paddingValues)
            .then(modifier)
    ) {
        content()
    }
}