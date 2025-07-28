package com.youhajun.transcall.core.ui.components.image

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MultipleCircleProfileImage(
    modifier: Modifier = Modifier,
    imageUrls: ImmutableList<String>,
    circleSize: Dp = 40.dp,
    circleBorderWidth: Dp = 2.dp,
    circleBorderColor: Color = Colors.White,
    overlap: Dp = 12.dp,
    maxVisibleCount: Int = 4,
    onClick: (Int) -> Unit = {},
) {
    val visibleCount = minOf(imageUrls.size, maxVisibleCount)
    val overflowCount = imageUrls.size - maxVisibleCount

    Row(modifier = modifier) {
        imageUrls.take(visibleCount).forEachIndexed { index, url ->
            key(index) {
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_person),
                    placeholder = painterResource(id = R.drawable.ic_person),
                    modifier = Modifier
                        .noRippleClickable { onClick(index) }
                        .zIndex(index.toFloat())
                        .offset(x = (-index * overlap))
                        .size(circleSize)
                        .clip(CircleShape)
                        .border(circleBorderWidth, circleBorderColor, CircleShape)
                )
            }
        }

        if (overflowCount > 0) {
            Box(
                modifier = Modifier
                    .zIndex(visibleCount.toFloat())
                    .offset(x = (-visibleCount * overlap))
                    .size(circleSize)
                    .clip(CircleShape)
                    .background(Colors.Gray700)
                    .border(2.dp, Colors.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+$overflowCount",
                    color = Colors.White,
                    style = Typography.bodySmall.copy(
                        fontWeight = FontWeight.W800
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun MultipleCircleProfileImagePreview() {
    MultipleCircleProfileImage(
        imageUrls = persistentListOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg",
            "https://example.com/image3.jpg",
            "https://example.com/image4.jpg",
            "https://example.com/image5.jpg"
        ),
        circleSize = 40.dp,
        overlap = 12.dp,
        maxVisibleCount = 4
    )
}