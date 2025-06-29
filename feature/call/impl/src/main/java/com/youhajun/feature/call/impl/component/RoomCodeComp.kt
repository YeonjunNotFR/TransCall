package com.youhajun.feature.call.impl.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.util.noRippleClickable

@Composable
internal fun RoomCodeComp(
    roomCode: String,
    onClickCopy: () -> Unit,
    onClickShare: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Colors.White, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.waiting_room_code_header),
            color = Colors.Gray500,
            style = Typography.titleMedium.copy(
                fontWeight = FontWeight.W500
            )
        )

        VerticalSpacer(8.dp)

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = roomCode,
                color = Colors.Black,
                style = Typography.displayMedium
            )

            HorizontalSpacer(16.dp)

            Icon(
                painter = painterResource(id = R.drawable.ic_home),
                contentDescription = null,
                modifier = Modifier
                    .noRippleClickable(
                        onClick = onClickCopy
                    )
                    .size(32.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_home),
                contentDescription = null,
                modifier = Modifier
                    .noRippleClickable(onClick = onClickShare)
                    .size(32.dp)
            )
        }
    }
}