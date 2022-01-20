/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package edu.bupt.ticketextraction.settings

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.bupt.ticketextraction.main.MainActivity

/**
 * MainActivity中设置页面的UI
 */
@ExperimentalMaterialApi
@Composable
fun SettingsUI(fatherActivity: MainActivity) {
    // 创建滚动条，虽然对于这个页面应该没啥用
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
    ) {
        SettingsListItem("账号管理") {
            // 登录成功跳转到个人信息
            // 未登录跳转到登录界面
            if (LoginActivity.loginState) {
                fatherActivity.jumpFromMainToPersonInfo()
            } else {
                fatherActivity.jumpFromMainToLogin()
            }
        }
        SettingsListItem("检查更新") {
            // TODO: 2022/1/15
        }
        SettingsListItem("清空缓存") {
            // TODO: 2022/1/15
        }
        SettingsListItem("关于我们") {
            fatherActivity.jumpFromMainToAboutUs()
        }
        SettingsListItem("访问网页端") {
            // TODO: 2022/1/15
        }
    }
}

/**
 * 设置页面的ListItem，具备点击事件
 *
 * @param text ListItem展示的文本
 * @param onClick 点击事件回调
 */
@Composable
@ExperimentalMaterialApi
private fun SettingsListItem(text: String, onClick: () -> Unit) {
    ListItem(Modifier.clickable { onClick() }) {
        Text(
            text = text,
            Modifier.padding(vertical = 10.dp),
            fontSize = 20.sp
        )
    }
    Divider()
}