package com.youhajun.feature.main.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.Typography
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import com.youhajun.transcall.core.ui.components.modifier.topBorder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun MainBottomBar(
    visibility: Boolean,
    currentTab: MainTab,
    tabs: ImmutableList<MainTab>,
    onClickMainTab: (MainTab) -> Unit,
) {
    if (visibility) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .topBorder(1.dp, Colors.FFE5E7EB),
        ) {
            tabs.forEach { tab ->
                if (tab.isCenter) {
                    CenterTabItem(
                        tab = tabs.first { it.isCenter },
                        isSelected = currentTab.isCenter,
                        onClick = onClickMainTab,
                    )
                } else {
                    MainTabItem(
                        tab = tab,
                        isSelected = tab.route == currentTab.route,
                        onClick = onClickMainTab,
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.MainTabItem(
    tab: MainTab,
    isSelected: Boolean,
    onClick: (MainTab) -> Unit,
) {
    val textColor = if (isSelected) Colors.FF637BE8 else Colors.FF808080
    val iconTintColor = if (isSelected) Colors.FF98AAFE else Colors.FF808080

    Column(
        Modifier
            .weight(1f)
            .fillMaxHeight()
            .noRippleClickable { onClick(tab) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = tab.iconResId),
            tint = iconTintColor,
            contentDescription = null
        )

        VerticalSpacer(4.dp)

        Text(
            text = stringResource(tab.stringResId),
            color = textColor,
            style = Typography.bodySmall
        )

        VerticalSpacer(5.dp)
    }
}

@Composable
private fun RowScope.CenterTabItem(
    tab: MainTab,
    isSelected: Boolean,
    onClick: (MainTab) -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .noRippleClickable { onClick(tab) },
        contentAlignment = Alignment.Center,
    ) {

        val textColor = if (isSelected) Colors.FF637BE8 else Colors.FF808080

        Box(
            modifier = Modifier
                .size(48.dp)
                .offset(y = (-24).dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    ambientColor = Colors.FF2F80ED,
                    spotColor = Colors.FF2F80ED,
                    clip = false
                )
                .background(Colors.FF98AAFE, CircleShape)
                .noRippleClickable { onClick(tab) },
        ) {
            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.Center),
                painter = painterResource(id = tab.iconResId),
                tint = Colors.White,
                contentDescription = null
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .noRippleClickable { onClick(tab) },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            VerticalSpacer(4.dp)

            Text(
                text = stringResource(tab.stringResId),
                color = textColor,
                style = Typography.bodySmall
            )

            VerticalSpacer(5.dp)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MainBottomBarPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        MainBottomBar(
            visibility = true,
            currentTab = MainTab.Home,
            tabs = MainTab.tabList().toImmutableList(),
            onClickMainTab = {}
        )
    }
}