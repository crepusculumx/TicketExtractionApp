package edu.bupt.ticketextraction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class SettingsFragment(val fatherActivity: MainActivity) {

}

@ExperimentalMaterialApi
@Preview
@Composable
fun SettingsUI() {
    val items = listOf("账号管理", "检查更新", "清空缓存", "关于我们", "访问网页端")
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
    ) {
        items.forEach {
            ListItem(Modifier.clickable { }) {
                Text(
                    text = it,
                    Modifier.padding(vertical = 10.dp)
                )
            }
            Divider()
        }
    }
}