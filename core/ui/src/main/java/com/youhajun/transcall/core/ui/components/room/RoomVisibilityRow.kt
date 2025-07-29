package com.youhajun.transcall.core.ui.components.room

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.room.RoomVisibility
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.indicator.RowIndicatorBox
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun RoomVisibilityRow(
    roomVisibilityList: ImmutableList<RoomVisibility> = RoomVisibility.entries.toImmutableList(),
    selectedRoomVisibility: RoomVisibility,
    onClick: (RoomVisibility) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Colors.FFF7F7F7, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        RowIndicatorBox(
            modifier = Modifier.fillMaxSize(),
            itemList = roomVisibilityList,
            selectedItem = selectedRoomVisibility,
            indicatorColor = Colors.FFFF5724,
            indicatorShape = RoundedCornerShape(8.dp),
            onClick = onClick,
            itemContent = { roomVisibility, isSelected ->
                RoomVisibilityItem(
                    roomVisibility = roomVisibility,
                    isSelected = isSelected,
                )
            }
        )
    }
}

@Composable
private fun RoomVisibilityItem(
    roomVisibility: RoomVisibility,
    isSelected: Boolean,
) {
    val selectedContentColor = if (isSelected) Colors.White else Colors.FF8C8D8B
    val selectedBackgroundColor = if (isSelected) Colors.FFFF5724 else Colors.TRANSPARENT

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(selectedBackgroundColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {

        Icon(
            modifier = Modifier.size(16.dp),
            tint = selectedContentColor,
            painter = painterResource(id = R.drawable.ic_clock),
            contentDescription = null
        )

        HorizontalSpacer(4.dp)

        Text(
            text = getStringResource(roomVisibility),
            color = selectedContentColor,
            style = Typography.bodyMedium.copy(fontWeight = FontWeight.W700),
        )
    }
}

@Composable
private fun getStringResource(roomVisibility: RoomVisibility): String {
    val res = when (roomVisibility) {
        RoomVisibility.PUBLIC -> R.string.create_room_visibility_public
        RoomVisibility.PRIVATE -> R.string.create_room_visibility_private
    }
    return stringResource(res)
}

@Preview
@Composable
private fun RoomVisibilityRowPreview() {
    var selectedRoomVisibility by remember { mutableStateOf(RoomVisibility.PUBLIC) }
    RoomVisibilityRow(
        selectedRoomVisibility = selectedRoomVisibility,
        onClick = { selectedRoomVisibility = it },
    )
}