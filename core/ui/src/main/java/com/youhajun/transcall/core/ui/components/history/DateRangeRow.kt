package com.youhajun.transcall.core.ui.components.history

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.youhajun.core.model.DateRange
import com.youhajun.core.ui.R
import com.youhajun.transcall.core.ui.theme.Colors
import com.youhajun.transcall.core.ui.theme.Typography
import com.youhajun.transcall.core.ui.util.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun DateRangeRow(
    dateRangeList: ImmutableList<DateRange> = DateRange.entries.toImmutableList(),
    selectedDateRange: DateRange,
    height: Dp = 40.dp,
    backgroundColor: Color = Colors.White,
    indicatorColor: Color = Colors.Primary,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    animationDuration: Int = 500,
    onClick: (DateRange) -> Unit,
    itemContent: @Composable (dateRange: DateRange, isSelected: Boolean) -> Unit = { dateRange, isSelected ->
        DefaultDateRangeItem(
            dateRange = dateRange,
            isSelected = isSelected,
        )
    }
) {

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(backgroundColor)
    ) {
        val tabCount = dateRangeList.size
        val tabWidth = maxWidth / tabCount
        val selectedIndex = dateRangeList.indexOf(selectedDateRange)

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
                .background(indicatorColor)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            dateRangeList.forEachIndexed { tabIndex, dateRange ->
                val isSelected = roundDisplayIndex == tabIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .noRippleClickable {
                            if (!isSelected) { onClick(dateRange) }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    itemContent(dateRange, isSelected)
                }
            }
        }
    }
}

@Composable
private fun DefaultDateRangeItem(
    dateRange: DateRange,
    isSelected: Boolean,
) {
    Text(
        text = getStringResource(dateRange),
        color = if (isSelected) Colors.White else Colors.Black,
        style = Typography.bodyLarge.copy(fontWeight = if (isSelected) FontWeight.W800 else FontWeight.W400),
    )
}

@Composable
private fun getStringResource(dateRange: DateRange): String {
    val res = when (dateRange) {
        DateRange.Week -> R.string.common_week_range
        DateRange.Month -> R.string.common_month_range
        DateRange.All -> R.string.common_all_range
    }
    return stringResource(res)
}

@Preview
@Composable
private fun DateRangeRowPreview() {
    var selectedDateRange by remember { mutableStateOf(DateRange.Week) }
    DateRangeRow(
        selectedDateRange = selectedDateRange,
        onClick = { selectedDateRange = it },
    )
}