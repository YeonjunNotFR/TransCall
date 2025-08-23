package com.youhajun.transcall.core.ui.components.calling

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.design.toDisplayIcon
import com.youhajun.core.design.toDisplayName
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.room.CurrentParticipant
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.modifier.speakingGlow

@Composable
fun DefaultVideoPlaceHolder(
    modifier: Modifier = Modifier,
    isSpeaking: Boolean,
    currentParticipant: CurrentParticipant?,
    displayNameTextStyle: TextStyle = Typography.displayLarge.copy(
        fontWeight = FontWeight.W800,
    ),
    languageTextStyle: TextStyle = Typography.bodyLarge.copy(
        fontWeight = FontWeight.W500
    ),
    languageIconSize: Dp = 24.dp,
    maxBlurValue: Dp = 50.dp,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = currentParticipant?.imageUrl,
            contentDescription = null,
            error = painterResource(id = R.drawable.ic_person),
            placeholder = painterResource(id = R.drawable.ic_person),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .speakingGlow(isSpeaking, CircleShape, maxBlurDp = maxBlurValue)
                .padding(1.dp)
                .clip(CircleShape)
        )

        VerticalSpacer(24.dp)

        Text(
            text = currentParticipant?.displayName ?: "",
            color = Colors.White,
            style = displayNameTextStyle
        )

        if(currentParticipant != null) {
            VerticalSpacer(8.dp)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(currentParticipant.language.toDisplayIcon()),
                    contentDescription = null,
                    modifier = Modifier.size(languageIconSize)
                )

                HorizontalSpacer(4.dp)

                Text(
                    text = stringResource(currentParticipant.language.toDisplayName()),
                    color = Colors.Gray500,
                    style = languageTextStyle,
                )
            }
        }
    }
}

@Preview
@Composable
private fun VideoPlaceHolderPreview() {
    DefaultVideoPlaceHolder(
        modifier = Modifier.fillMaxSize().background(Colors.SurfaceDark),
        isSpeaking = true,
        currentParticipant = CurrentParticipant(
            userId = "1",
            displayName = "John Doe",
            imageUrl = "https://example.com/image.jpg",
            language = LanguageType.ENGLISH,
        ),
    )
}