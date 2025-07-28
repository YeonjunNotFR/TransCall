package com.youhajun.transcall.core.ui.components.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.DateRange
import com.youhajun.transcall.core.ui.components.indicator.RowIndicatorBox
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun DateRangeRow(
    dateRangeList: ImmutableList<DateRange> = DateRange.entries.toImmutableList(),
    selectedDateRange: DateRange,
    onClick: (DateRange) -> Unit,
) {
    RowIndicatorBox(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Colors.White, RoundedCornerShape(12.dp)),
        itemList = dateRangeList,
        selectedItem = selectedDateRange,
        indicatorColor = Colors.FF60A5FA,
        indicatorShape = RectangleShape,
        onClick = onClick,
        itemContent = { dateRange, isSelected ->
            DateRangeItem(
                dateRange = dateRange,
                isSelected = isSelected,
            )
        }
    )
}

@Composable
private fun DateRangeItem(
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