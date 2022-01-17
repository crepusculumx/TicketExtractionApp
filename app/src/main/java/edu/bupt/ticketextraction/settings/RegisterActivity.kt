/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package edu.bupt.ticketextraction.settings

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import edu.bupt.ticketextraction.ui.compose.*

/**
 * 本Activity用于处理用户注册，包括两个页面，
 * 一个页面输入手机号和验证码，另一个输入密码和重复输入密码
 */
class RegisterActivity : ComponentActivity() {
    // TODO: 2022/1/17 这里活动的返回键需要自定义一下，主要是在第二个页面返回应该到第一个页面
    @SuppressLint("UnrememberedMutableState")
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                Scaffold(topBar = { TopBarWithTitleAndBack("注册") }) {
                    // 当前是否位于第一个页面
                    var isFirstButton by remember { mutableStateOf(true) }
                    // 注册的手机号
                    val phoneNumber = mutableStateOf("")
                    // 注册的密码
                    val password = mutableStateOf("")
                    // 重复密码
                    val rePassword = mutableStateOf("")
                    val navController = rememberAnimatedNavController()
                    Box(modifier = Modifier.fillMaxHeight()) {
                        AnimatedNavHost(navController = navController,
                            startDestination = "1",
                            modifier = Modifier.align(Alignment.TopCenter),
                            // 进入动画，从屏幕右边加载到中间
                            enterTransition = { slideInHorizontally { it } },
                            // 弹出时上一个页面的进入动画，从屏幕左边加载到中间
                            popEnterTransition = { slideInHorizontally { -it } },
                            // 离开动画，从中间移动到屏幕左边
                            exitTransition = { slideOutHorizontally { -it } },
                            // 弹出时本页面离开动画，从中间移动到屏幕右边
                            popExitTransition = { slideOutHorizontally { it } }
                        ) {
                            composable("1") {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 80.dp)
                                ) {
                                    // 输入手机号的编辑框
                                    PhoneNumberTextField(
                                        phoneNumber = phoneNumber,
                                        onValueChange = { phoneNumber.value = it },
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
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
                                        isFirstButton = false
                                    }
                                }
                            }
                            composable("2") {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 80.dp)
                                ) {
                                    // 输入密码
                                    PasswordTextField(
                                        password = password,
                                        onValueChange = { password.value = it },
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(bottom = 30.dp)
                                    )
                                    // 重新输入密码
                                    PasswordTextField(
                                        password = rePassword,
                                        onValueChange = { rePassword.value = it },
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                    // 注册，把参数传递一下
                                    RoundedCornerButton(
                                        text = "注册", modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(top = 40.dp)
                                            .size(width = 150.dp, height = 100.dp)
                                    ) {
                                        // TODO: 2022/1/17 注册
                                    }
                                }
                            }
                        }
                        // 底部导航按钮，只是表明现在在第几步
                        NavigationButtons(
                            isFirstButton = isFirstButton,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationButtons(isFirstButton: Boolean, modifier: Modifier) {
    val materialColor = MaterialTheme.colors
    val pressedColors = ButtonDefaults.buttonColors(disabledBackgroundColor = materialColor.primary)
    val notPressedColors = ButtonDefaults.buttonColors()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 150.dp)
    ) {
        Button(
            onClick = { },
            enabled = false,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 80.dp),
            shape = RoundedCornerShape(100),
            colors = if (isFirstButton)
                pressedColors
            else notPressedColors
        ) {
            Text(text = "1", fontSize = 22.sp)
        }
        Button(
            onClick = { },
            enabled = false,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 80.dp),
            shape = RoundedCornerShape(100),
            colors = if (isFirstButton)
                notPressedColors
            else pressedColors
        ) {
            Text(text = "2", fontSize = 22.sp)
        }
    }
}