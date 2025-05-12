package com.youhajun.transcall.core.ui.components.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youhajun.core.model.CallHistory
import com.youhajun.core.ui.R
import com.youhajun.transcall.core.ui.components.CircleAsyncImage
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.theme.Colors
import com.youhajun.transcall.core.ui.theme.Typography
import com.youhajun.transcall.core.ui.util.DateFormatPatterns
import com.youhajun.transcall.core.ui.util.TimeFormatPatterns
import com.youhajun.transcall.core.ui.util.noRippleClickable
import com.youhajun.transcall.core.ui.util.toUiDateString
import com.youhajun.transcall.core.ui.util.toUiDurationString

@Composable
fun CallHistoryAtHomeItem(
    callHistory: CallHistory,
    onClickCallAgain: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Colors.Gray300,
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleAsyncImage(
            imageUrl = callHistory.partnerImageUrl,
            size = 40.dp,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Row {
                Text(
                    text = callHistory.partnerName,
                    color = Color.Black,
                    style = Typography.bodyLarge,
                )

                Text(
                    text = callHistory.partnerLangType.code,
                    color = Colors.Gray700,
                    style = Typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            VerticalSpacer(4.dp)

            Row {
                Text(
                    text = callHistory.startedAtEpochSeconds.toUiDateString(DateFormatPatterns.DATE_TIME_WITHOUT_SECONDS),
                    color = Colors.Gray700,
                    style = Typography.bodySmall,
                    modifier = Modifier.alignByBaseline()
                )

                HorizontalSpacer(8.dp)

                Text(
                    text = callHistory.durationSeconds.toUiDurationString(TimeFormatPatterns.LABEL_HOUR_MIN_SEC),
                    color = Colors.Black,
                    style = Typography.bodySmall,
                    modifier = Modifier.alignByBaseline()
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_home),
            contentDescription = null,
            modifier = Modifier.size(24.dp).noRippleClickable { onClickCallAgain(callHistory.callId) }
        )
    }
}

@Composable
fun CallHistoryItem(
    callHistory: CallHistory,
    onClickCallAgain: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Colors.Gray300,
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleAsyncImage(
            imageUrl = callHistory.partnerImageUrl,
            size = 40.dp,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = callHistory.partnerName,
                color = Color.Black,
                style = Typography.bodyLarge,
            )

            VerticalSpacer(4.dp)

            Row {
                Text(
                    text = callHistory.startedAtEpochSeconds.toUiDateString(DateFormatPatterns.DATE_TIME_WITHOUT_SECONDS),
                    color = Colors.Gray700,
                    style = Typography.bodySmall,
                    modifier = Modifier.alignByBaseline()
                )

                HorizontalSpacer(8.dp)

                Text(
                    text = callHistory.durationSeconds.toUiDurationString(TimeFormatPatterns.LABEL_HOUR_MIN_SEC),
                    color = Colors.Black,
                    style = Typography.bodySmall,
                    modifier = Modifier.alignByBaseline()
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_home),
            contentDescription = null,
            modifier = Modifier.size(24.dp).noRippleClickable { onClickCallAgain(callHistory.callId) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallHistoryItemAtHomePreview() {
    CallHistoryAtHomeItem(
        callHistory = CallHistory(
            callId = "12345",
            partnerName = "John Doe",
            partnerImageUrl = "https://example.com/image.jpg",
            startedAtEpochSeconds = 1633072800L,
            durationSeconds = 12001,
        ),
        onClickCallAgain = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallHistoryItemPreview() {
    CallHistoryItem(
        callHistory = CallHistory(
            callId = "12345",
            partnerName = "John Doe",
            partnerImageUrl = "https://example.com/image.jpg",
            startedAtEpochSeconds = 1633072800L,
            durationSeconds = 12001,
        ),
        onClickCallAgain = {}
    )
}