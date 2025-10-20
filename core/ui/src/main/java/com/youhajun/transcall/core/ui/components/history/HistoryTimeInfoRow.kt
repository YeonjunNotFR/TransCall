package com.youhajun.transcall.core.ui.components.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.util.DateFormatPatterns
import com.youhajun.transcall.core.ui.util.TimePatterns
import com.youhajun.transcall.core.ui.util.toUiDateString
import com.youhajun.transcall.core.ui.util.toUiDurationString

@Composable
fun HistoryTimeInfoRow(
    modifier: Modifier,
    joinedAtToEpochTime: Long,
    leftAtToEpochTime: Long?,
    durationSeconds: Int?,
    dateFormat: DateFormatPatterns = DateFormatPatterns.YEAR_MONTH_DAY_HOUR_MINUTE,
    timeFormat: TimePatterns = TimePatterns.LABEL_HOUR_MIN_SEC
) {

    val joinedAtText = joinedAtToEpochTime.toUiDateString(dateFormat)
    val leftAtText = leftAtToEpochTime?.toUiDateString(dateFormat)
    val timeText = durationSeconds?.toUiDurationString(timeFormat)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconAndText(
            text = buildString {
                append(joinedAtText)
                leftAtText?.let { append(" - ").append(it) }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Colors.FF6B7280,
                    modifier = Modifier.size(12.dp)
                )
            }
        )

        if (timeText != null) {
            IconAndText(
                text = timeText,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_clock),
                        contentDescription = null,
                        tint = Colors.FF6B7280,
                        modifier = Modifier.size(12.dp)
                    )
                }
            )
        }
    }
}


@Composable
private fun IconAndText(
    text: String,
    icon: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .background(Colors.FFEEF6FF, RoundedCornerShape(28.dp))
            .border(1.dp, Colors.FFE6E9EE, RoundedCornerShape(28.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        HorizontalSpacer(2.dp)

        Text(
            text = text,
            color = Colors.FF6B7280,
            style = Typography.bodySmall
        )
    }
}