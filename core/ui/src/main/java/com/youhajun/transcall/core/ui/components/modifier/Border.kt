package com.youhajun.transcall.core.ui.components.modifier

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.topBorder(
    strokeWidth: Dp,
    color: Color
) = this then Modifier.drawBehind {
    val strokeWidthPx = density.run { strokeWidth.toPx() }

    drawLine(
        color = color,
        start = Offset(x = 0f, y = strokeWidthPx / 2),
        end = Offset(x = size.width, y = strokeWidthPx / 2),
        strokeWidth = strokeWidthPx
    )
}

@Composable
fun Modifier.bottomBorder(
    strokeWidth: Dp,
    color: Color,
    padding: Dp = 0.dp
) = this then Modifier.drawBehind {
    val strokeWidthPx = density.run { strokeWidth.toPx() }

    drawLine(
        color = color,
        start = Offset(x = 0f, y = size.height - strokeWidthPx / 2 + padding.toPx()),
        end = Offset(x = size.width, y = size.height - strokeWidthPx / 2 + padding.toPx()),
        strokeWidth = strokeWidthPx
    )
}

fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round
) = this then Modifier.drawWithContent {

    val outline = shape.createOutline(size, layoutDirection, density = this)

    val dashedStroke = Stroke(
        cap = cap,
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx())
        )
    )

    drawContent()

    drawOutline(
        outline = outline,
        style = dashedStroke,
        color = color
    )
}