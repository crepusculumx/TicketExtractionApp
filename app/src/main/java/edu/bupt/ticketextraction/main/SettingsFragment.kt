package edu.bupt.ticketextraction.main

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
import androidx.compose.ui.unit.sp

class SettingsFragment(val fatherActivity: MainActivity) {

}

sealed class Settings(var name: String, var onClick: () -> Unit) {
    object Account : Settings("账号管理", {})
    object Update : Settings("检查更新", {})
    object ClearCache : Settings("清空缓存", {})
    object AboutUs : Settings("关于我们", {})
    object VisitWeb : Settings("访问网页端", {})
}

@ExperimentalMaterialApi
@Preview
@Composable
fun SettingsUI() {
    val items = listOf(
        Settings.Account, Settings.Update, Settings.ClearCache,
        Settings.AboutUs, Settings.VisitWeb
    )
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
    ) {
        items.forEach {
            ListItem(Modifier.clickable { it.onClick }) {
                Text(
                    text = it.name,
                    Modifier.padding(vertical = 10.dp),
                    fontSize = 20.sp
                )
            }
            Divider()
        }
    }
}