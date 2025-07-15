package com.youhajun.transcall.core.ui.components.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youhajun.core.model.calling.CallHistory
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.room.Participant
import com.youhajun.transcall.core.ui.components.CircleAsyncImage
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.core.design.Colors
import com.youhajun.core.design.Typography
import com.youhajun.core.design.R
import com.youhajun.transcall.core.ui.util.DateFormatPatterns
import com.youhajun.transcall.core.ui.util.TimeFormatPatterns
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import com.youhajun.transcall.core.ui.util.toUiDateString
import com.youhajun.transcall.core.ui.util.toUiDurationString

@Composable
fun CallHistoryItem(
    callHistory: CallHistory,
    dateFormat: DateFormatPatterns = DateFormatPatterns.DATE_TIME_WITHOUT_SECONDS,
    onClickCallAgain: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Colors.FFB2F2FF,
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleAsyncImage(
            imageUrl = callHistory.partner?.imageUrl,
            size = 40.dp,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = callHistory.partner?.displayName ?: "",
                    color = Color.Black,
                    style = Typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                HorizontalSpacer(4.dp)

                Text(
                    text = "(${callHistory.partner?.language?.code?.uppercase() ?: ""})",
                    color = Colors.PrimaryLight,
                    style = Typography.bodySmall.copy(
                        fontWeight = FontWeight.W800
                    ),
                    softWrap = false,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            VerticalSpacer(4.dp)

            Row {
                Text(
                    text = callHistory.startedAtEpochSeconds.toUiDateString(dateFormat),
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

        Icon(
            painter = painterResource(id = R.drawable.ic_home),
            contentDescription = null,
            modifier = Modifier.noRippleClickable {
                callHistory.partner?.userId?.let { userId ->
                    onClickCallAgain(userId)
                }
            }.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallHistoryFullDateItemPreview() {
    CallHistoryItem(
        callHistory = CallHistory(
            callId = "12345",
            partner = Participant(
                userId = "67890",
                displayName = "John Doe dwaafwojfdkawdkadwkd dawkawd akwdkaw dawkd wa dwakd",
                imageUrl = "https://example.com/image.jpg",
                language = LanguageType.ENGLISH
            ),
            startedAtEpochSeconds = 1633072800L,
            durationSeconds = 12001,
        ),
        dateFormat = DateFormatPatterns.DATE_TIME_WITHOUT_SECONDS,
        onClickCallAgain = {}
    )
}