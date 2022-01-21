/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package edu.bupt.ticketextraction.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.bupt.ticketextraction.ui.compose.ActivityBody
import edu.bupt.ticketextraction.ui.compose.PasswordTextField
import edu.bupt.ticketextraction.ui.compose.RoundedCornerButton
import edu.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

/**
 * 修改密码Activity，用户登录后才能访问到
 */
class ChangePasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                Scaffold(topBar = { TopBarWithTitleAndBack("修改密码") { finish() } }) {
                    var oldPassword by remember { mutableStateOf("") }
                    var newPassword by remember { mutableStateOf("") }
                    var newRePassword by remember { mutableStateOf("") }
                    Column(
                        modifier = Modifier
                            .padding(top = 80.dp)
                            .fillMaxWidth()
                    ) {
                        val ch = Alignment.CenterHorizontally
                        // 旧密码
                        PasswordTextField(
                            password = oldPassword,
                            placeholder = "请输入旧密码",
                            modifier = Modifier.align(ch),
                            onValueChange = { oldPassword = it }
                        )
                        // 新密码
                        PasswordTextField(
                            password = newPassword,
                            placeholder = "请输入新密码",
                            modifier = Modifier.align(ch),
                            onValueChange = { newPassword = it }
                        )
                        // 重复新密码
                        PasswordTextField(
                            password = newRePassword,
                            placeholder = "请重复输入新密码",
                            modifier = Modifier.align(ch),
                            onValueChange = { newRePassword = it }
                        )
                        // 提交按钮
                        RoundedCornerButton(text = "修改", modifier = Modifier.align(ch)) {
                            // TODO: 2022/1/21 修改密码
                        }
                    }
                }
            }
        }
    }
}