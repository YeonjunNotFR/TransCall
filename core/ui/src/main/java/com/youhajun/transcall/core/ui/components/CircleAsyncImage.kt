package com.youhajun.transcall.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CircleAsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    size: Dp = 48.dp,
    placeholder: Painter? = null,
    error: Painter? = null
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        placeholder = placeholder,
        error = error,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape)
            .size(size)
    )
}