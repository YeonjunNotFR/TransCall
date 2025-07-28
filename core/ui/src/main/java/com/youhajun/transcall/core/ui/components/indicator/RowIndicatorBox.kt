package com.youhajun.transcall.core.ui.components.indicator

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.IntOffset
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import kotlinx.collections.immutable.ImmutableList

@Composable
fun <T> RowIndicatorBox(
    modifier: Modifier = Modifier,
    itemList: ImmutableList<T>,
    selectedItem: T,
    indicatorColor: Color,
    indicatorShape: Shape,
    animationDuration: Int = 500,
    onClick: (T) -> Unit,
    itemContent: @Composable (item: T, isSelected: Boolean) -> Unit
) {

    BoxWithConstraints(modifier = modifier) {
        val tabCount = itemList.size
        val tabWidth = maxWidth / tabCount
        val selectedIndex = itemList.indexOf(selectedItem)

        val animeTabPosition by animateFloatAsState(
            targetValue = selectedIndex.toFloat(),
            animationSpec = tween(
                durationMillis = animationDuration,
                easing = FastOutSlowInEasing
            )
        )
        val indicatorOffsetX = tabWidth * animeTabPosition
        val roundDisplayIndex = ((animeTabPosition + 0.5f).toInt()).coerceIn(0, tabCount - 1)

        Box(
            modifier = Modifier
                .offset { IntOffset(indicatorOffsetX.roundToPx(), 0) }
                .width(tabWidth)
                .fillMaxHeight()
                .clip(indicatorShape)
                .background(indicatorColor)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            itemList.forEachIndexed { index, item ->
                val isSelected = roundDisplayIndex == index

                key(index) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .noRippleClickable {
                                if (!isSelected) onClick(item)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        itemContent(item, isSelected)
                    }
                }
            }
        }
    }
}