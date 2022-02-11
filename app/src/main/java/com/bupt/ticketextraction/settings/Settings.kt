/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.settings

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.main.MainActivity
import com.bupt.ticketextraction.main.StartActivity
import com.bupt.ticketextraction.network.downloadApk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * web端网址
 */
private const val webAddress = "https://www.baidu.com"

var isLatestVersion = mutableStateOf(true)

/**
 * 从MainActivity跳转到LoginActivity
 *
 * @receiver MainActivity
 */
fun MainActivity.jumpToLogin() {
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
}

/**
 * 从MainActivity跳转到PersonInfoActivity
 *
 * @receiver MainActivity
 */
fun MainActivity.jumpToPersonInfo() {
    val intent = Intent(this, PersonInfoActivity::class.java)
    startActivity(intent)
}

/**
 * 从MainActivity跳转到AboutUsActivity
 *
 * @receiver MainActivity
 */
fun MainActivity.jumpToAboutUs() {
    val intent = Intent(this, AboutUsActivity::class.java)
    startActivity(intent)
}

/**
 * MainActivity中设置页面的UI
 *
 * @param fatherActivity 父活动
 */
@ExperimentalMaterialApi
@Composable
fun SettingsUI(fatherActivity: MainActivity) {
    var isDialogShow by remember { mutableStateOf(false) }
    // 创建滚动条，虽然对于这个页面应该没啥用
    LazyColumn(Modifier.fillMaxWidth()) {
        SettingsListItem("账号管理") {
            // 登录成功跳转到个人信息
            // 未登录跳转到登录界面
            if (LoginActivity.loginState) {
                fatherActivity.jumpToPersonInfo()
            } else {
                fatherActivity.jumpToLogin()
            }
        }
        val text = if (isLatestVersion.value) "已是最新版本" else "存在最新版本"
        SettingsListItem("检查更新", { Text(text, fontSize = 15.sp) }) {
            fatherActivity.launch {
                runBlocking {
                    withContext(Dispatchers.IO) {
                        StartActivity.checkUpdate()
                    }
                }
                if (isLatestVersion.value.not()) {
                    isDialogShow = true
                } else {
                    Toast.makeText(fatherActivity, text, Toast.LENGTH_SHORT).show()
                }
            }
        }
        SettingsListItem("清空缓存") {
            // TODO: 2022/1/15
        }
        SettingsListItem("关于我们") {
            fatherActivity.jumpToAboutUs()
        }
        SettingsListItem("访问网页端") {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = Uri.parse(webAddress)
            intent.data = uri
            fatherActivity.startActivity(intent)
        }
    }
    if (isDialogShow) {
        // 更新弹窗
        AlertDialog(
            title = { Text("存在最新版本，是否更新？") },
            onDismissRequest = { isDialogShow = false },
            confirmButton = {
                TextButton(onClick = {
                    downloadApk(fatherActivity)
                    isDialogShow = false
                }) { Text("更新") }
            },
            dismissButton = { TextButton(onClick = { isDialogShow = false }) { Text("取消") } }
        )
    }
}

/**
 * 设置页面的ListItem，具备点击事件
 *
 * @param text ListItem展示的文本
 * @param onClick 点击事件回调
 */
@ExperimentalMaterialApi
private fun LazyListScope.SettingsListItem(text: String, trailing: @Composable () -> Unit = {}, onClick: () -> Unit) {
    item {
        ListItem(Modifier.clickable { onClick() }, trailing = { trailing() }) {
            Text(
                text = text,
                Modifier.padding(vertical = 10.dp),
                fontSize = 20.sp
            )
        }
        Divider()
    }
}