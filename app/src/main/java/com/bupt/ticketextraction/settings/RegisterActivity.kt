/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.settings

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bupt.ticketextraction.network.register
import com.bupt.ticketextraction.ui.compose.*
import kotlinx.coroutines.*

/**
 * 本Activity用于处理用户注册和找回密码，包括两个页面，
 * 第一页输入手机号和验证码，第二页输入密码和重复输入密码
 */
@OptIn(ExperimentalAnimationApi::class)
class RegisterActivity : TwoStepsActivity(), CoroutineScope by MainScope() {
    companion object {
        /**
         * 当前是否是注册
         * true注册 false找回密码
         */
        var isRegister = true
    }

    /**
     * 以下三个字段用于记录用户输入结果
     */
    private var phoneNumber = mutableStateOf("")
    private var password = mutableStateOf("")
    private var rePassword = mutableStateOf("")

    /**
     * 是否展示正在注册中的Dialog
     */
    private var dialogIsShow = mutableStateOf(false)

    /**
     * password和rePassword的placeholder字符串中是否含有“新”，
     * 由是否是注册决定
     */
    private val placeholderInsert: String

    /**
     * 第二页提交账号和密码的按钮的文本
     */
    private val buttonText: String

    init {
        if (isRegister) {
            // 本Activity作为注册使用时
            title = "注册"
            placeholderInsert = ""
            buttonText = "注册"
        } else {
            // 本Activity作为找回密码使用时
            title = "找回密码"
            placeholderInsert = "新"
            buttonText = "修改"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 在RegisterActivity生命周期结束时销毁所有协程
        cancel()
    }

    @Composable
    override fun naviItem1() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp)
        ) {
            // 输入手机号的编辑框
            PhoneNumberTextField(
                phoneNumber = phoneNumber.value,
                onValueChange = { phoneNumber.value = it },
            )
            // 到下一步输入密码和重复密码的按钮
            RoundedCornerButton(
                text = "下一步", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 40.dp)
                    .size(width = 150.dp, height = 100.dp)
            ) {
                // 导航到2页面去，即输入密码和重复密码
                navController.navigate("2")
                isFirstButton.value = false
            }
        }
    }

    @Composable
    override fun naviItem2() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp)
        ) {
            // 输入密码
            PasswordTextField(
                password = password.value,
                placeholder = "请输入${placeholderInsert}密码",
            ) { password.value = it }
            // 重新输入密码
            PasswordTextField(
                password = rePassword.value,
                placeholder = "请重复输入${placeholderInsert}密码",
            ) { rePassword.value = it }
            // 注册，把参数传递一下
            RoundedCornerButton(
                text = buttonText, modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 40.dp)
                    .size(width = 150.dp, height = 100.dp)
            ) {
                launch {
                    val deferred = async { return@async register(phoneNumber.value, password.value) }
                    // 等待获取结果
                    when (deferred.await()) {
                        1 -> Toast.makeText(this@RegisterActivity, "注册成功", Toast.LENGTH_SHORT)
                            .show()
                        -1 -> Toast
                            .makeText(this@RegisterActivity, "手机号已存在！", Toast.LENGTH_SHORT)
                            .show()
                        -2 -> Toast.makeText(this@RegisterActivity, "未知错误", Toast.LENGTH_SHORT)
                            .show()
                        369 -> Toast.makeText(this@RegisterActivity, "网络连接失败！", Toast.LENGTH_SHORT)
                            .show()
                        // 不可达
                        else -> assert(false)
                    }
                    delay(200)
                    finish()
                }
                dialogIsShow.value = true
            }
        }
    }

    @Composable
    override fun content() {
        if (dialogIsShow.value) {
            ProgressDialog("正在注册中···") { dialogIsShow.value = false }
        }
    }
}
