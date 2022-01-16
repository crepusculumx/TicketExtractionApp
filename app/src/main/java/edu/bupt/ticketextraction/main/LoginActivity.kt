package edu.bupt.ticketextraction.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.bupt.ticketextraction.compose.ActivityBody
import edu.bupt.ticketextraction.compose.PasswordTextField
import edu.bupt.ticketextraction.compose.PhoneNumberTextField
import edu.bupt.ticketextraction.compose.TopBarWithTitleAndBack

/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */

/**
 * 此Activity用于处理用户登录，以及可以跳转到注册和找回密码
 */
class LoginActivity : ComponentActivity() {
    /**
     * 登录状态，true为已登录
     */
    var loginState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ActivityBody设置主题和颜色
            ActivityBody {
                Scaffold(
                    // 顶栏，返回键 + 登录标题
                    topBar = { TopBarWithTitleAndBack(title = "登录") },
                    // 底栏，注册账号 + 找回密码
                    bottomBar = {
                        RegisterAndFind()
                    }) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // 手机号编辑框
                        val phoneNumber = mutableStateOf("")
                        PhoneNumberTextField(
                            phoneNumber = phoneNumber,
                            onValueChange = { phoneNumber.value = it },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 80.dp, bottom = 10.dp)
                        )
                        // 密码编辑框
                        val password = mutableStateOf("")
                        PasswordTextField(
                            password = password,
                            onValueChange = { password.value = it },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        // 登录按钮
                        Button(
                            onClick = { /*TODO 2022/1/16 把phone和password验证一下*/ },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 40.dp)
                                .size(width = 100.dp, height = 50.dp),
                            // 来个圆角按钮
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(text = "登录", fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * 登录底部附加功能，注册新账号 + 找回密码
 */
@Composable
fun RegisterAndFind() {
    Box(
        modifier = Modifier
            .padding(bottom = 30.dp)
            .fillMaxWidth()
    ) {
        // 注册新账号按钮
        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.CenterStart)
                // 对称分布在左侧
                .padding(start = 50.dp)
        ) { BottomTextButtonText(text = "注册新账号") }
        // 这是一个竖直分割线，实在是找不到API了
        Text(
            text = "丨",
            fontSize = 26.sp,
            modifier = Modifier.align(Alignment.Center)
        )
        // 找回密码按钮
        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                // 对称分布在右侧
                .padding(end = 50.dp)
        ) { BottomTextButtonText(text = "找回密码") }
    }
}

/**
 * 底部附加功能文本, 16sp字体大小 + onBackground字体颜色
 *
 * @param text 文本内容
 */
@Composable
fun BottomTextButtonText(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        color = MaterialTheme.colors.onBackground
    )
}