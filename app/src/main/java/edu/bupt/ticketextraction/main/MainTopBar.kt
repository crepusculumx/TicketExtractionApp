package edu.bupt.ticketextraction.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * MainActivity的TopBar的更多按钮的DropdownMenu的Item，
 * 具备点击事件，一个图标 + 一个文本
 */
@Composable
fun MainTopMoreDropdownMenuItem(resId: Int, text: String, onClick: () -> Unit) {
    DropdownMenuItem(onClick = { onClick() }) {
        Icon(
            painterResource(id = resId),
            contentDescription = null,
            // 原尺寸(24dp)太大，改为20dp再设置5dp的距离
            modifier = Modifier
                .size(20.dp)
                .padding(end = 5.dp)
        )
        Text(text = text)
    }
}


