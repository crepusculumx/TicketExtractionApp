/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.settings

import android.os.Bundle
import android.widget.Toast
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
import com.bupt.ticketextraction.network.changePwd
import com.bupt.ticketextraction.network.getNetworkType
import com.bupt.ticketextraction.ui.compose.*
import com.bupt.ticketextraction.utils.passwordPattern
import kotlinx.coroutines.*

/**
 * 修改密码Activity，用户登录后才能访问到
 */
class ChangePasswordActivity : ComponentActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                Scaffold(topBar = { TopBarWithTitleAndBack("修改密码") { finish() } }) {
                    Column(
                        modifier = Modifier
                            .padding(top = 80.dp)
                            .fillMaxWidth()
                    ) {
                        val ch = Alignment.CenterHorizontally
                        var isDialogShow by remember { mutableStateOf(false) }
                        var oldPassword by remember { mutableStateOf("") }
                        var newPassword by remember { mutableStateOf("") }
                        var newRePassword by remember { mutableStateOf("") }
                        var isOldValid by remember { mutableStateOf(false) }
                        var isNewValid by remember { mutableStateOf(false) }
                        var isNewReValid by remember { mutableStateOf(false) }
                        // 旧密码
                        PasswordTextField(
                            password = oldPassword,
                            placeholder = "请输入旧密码",
                            onValueChange = {
                                oldPassword = it
                                isOldValid = true
                            }
                        )
                        // 新密码
                        PasswordTextField(
                            password = newPassword,
                            placeholder = "请输入新密码",
                            onValueChange = {
                                newPassword = it
                                isNewValid = passwordPattern.matcher(it).matches()
                            }
                        )
                        // 密码提示
                        PasswordInstruction()
                        // 重复新密码
                        PasswordTextField(
                            password = newRePassword,
                            placeholder = "请重复输入新密码",
                            onValueChange = {
                                newRePassword = it
                                isNewReValid = newPassword == newRePassword
                            }
                        )
                        // 重复密码提示
                        RePasswordInstruction()
                        // 提交按钮
                        RoundedCornerButton(
                            text = "修改",
                            // 三个都行才允许修改
                            enabled = isOldValid and isNewValid and isNewReValid,
                            modifier = Modifier.align(ch)
                        ) {
                            launch {
                                // 检查网络
                                when (getNetworkType()) {
                                    2 -> Toast.makeText(this@ChangePasswordActivity, "正在使用移动数据", Toast.LENGTH_SHORT)
                                        .show()

                                    369 -> {
                                        Toast.makeText(this@ChangePasswordActivity, "请检查网络连接", Toast.LENGTH_SHORT)
                                            .show()
                                        return@launch
                                    }
                                }
                                val deferred = async { changePwd(newPassword, oldPassword) }
                                when (deferred.await()) {
                                    1 -> {
                                        Toast.makeText(this@ChangePasswordActivity, "修改成功", Toast.LENGTH_SHORT)
                                            .show()
                                        // 延迟0.2s使得Activity别结束这么快
                                        delay(200)
                                        finish()
                                    }

                                    0 -> Toast.makeText(this@ChangePasswordActivity, "原密码错误！", Toast.LENGTH_SHORT)
                                        .show()

                                    -2 -> Toast.makeText(this@ChangePasswordActivity, "未知错误", Toast.LENGTH_SHORT)
                                        .show()

                                    else -> assert(false)
                                }
                                isDialogShow = false
                            }
                            isDialogShow = true
                        }
                        if (isDialogShow) {
                            ProgressDialog("正在修改中...") { isDialogShow = false }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 在Activity生命周期结束时销毁所有协程
        cancel()
    }
}