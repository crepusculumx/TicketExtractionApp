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
import android.view.KeyEvent
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
 * 本Activity用于处理用户注册和找回密码，包括两个页面，
 * 第一页输入手机号和验证码，第二页输入密码和重复输入密码
 */
class RegisterActivity : ComponentActivity() {
    companion object {
        /**
         * 当前是否是注册
         * true注册 false找回密码
         */
        var isRegister = true
    }

    /**
     * TopBar的标题名，由是否是注册决定
     */
    private val title: String

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

    /**
     * 注册一共有两个页面，一个是输入手机号和验证码，
     * 另一个是输入密码和重复密码，当为true时在第一个页面，
     * false时在第二个页面
     */
    private var isFirstButton = mutableStateOf(true)

    /**
     * 为了处理点击底部返回键覆写一下
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 当用户点击底部返回按钮时
        // 如果在第二个页面，即isFirstButton.value为false时
        // 修改为true，剩下的交给super
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isFirstButton.value) {
                isFirstButton.value = true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    @SuppressLint("UnrememberedMutableState")
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                // 当前是否位于第一个页面
                var isFirstButton by remember { isFirstButton }
                val navController = rememberAnimatedNavController()
                Scaffold(topBar = {
                    TopBarWithTitleAndBack(title) {
                        // 当位于第一个页面时，结束Activity
                        if (isFirstButton) {
                            finish()
                        } else {
                            // 当位于第二个页面时，跳转到第一个页面
                            isFirstButton = true
                            navController.popBackStack()
                        }
                    }
                }) {
                    // 注册的手机号
                    var phoneNumber by remember { mutableStateOf("") }
                    // 注册的密码
                    var password by remember { mutableStateOf("") }
                    // 重复密码
                    var rePassword by remember { mutableStateOf("") }
                    Box(modifier = Modifier.fillMaxHeight()) {
                        AnimatedNavHost(
                            navController = navController,
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
                                    var phoneNumberCopy by remember { mutableStateOf("") }
                                    // 输入手机号的编辑框
                                    PhoneNumberTextField(
                                        phoneNumber = phoneNumberCopy,
                                        onValueChange = { phoneNumberCopy = it },
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
                                        phoneNumber = phoneNumberCopy
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
                                        placeholder = "请输入${placeholderInsert}密码",
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(bottom = 30.dp)
                                    ) { password = it }
                                    // 重新输入密码
                                    PasswordTextField(
                                        password = rePassword,
                                        placeholder = "请重复输入${placeholderInsert}密码",
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    ) { rePassword = it }
                                    // 注册，把参数传递一下
                                    RoundedCornerButton(
                                        text = buttonText, modifier = Modifier
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

/**
 * 底部两个导航按钮
 *
 * @param isFirstButton 当前是否是第一个按钮被选中
 * @param modifier 让按钮在底部居中的位置
 */
@Composable
private fun NavigationButtons(isFirstButton: Boolean, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 150.dp)
    ) {
        // 按钮1，表明正处于第一步
        NavigationButton(
            isFirstButton = isFirstButton,
            // 写个1上去
            text = "1",
            // 分布在左侧
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 80.dp)
        )
        // 按钮2，表明正处于第二步
        NavigationButton(
            isFirstButton = !isFirstButton,
            // 写个2上去
            text = "2",
            // 分布在右侧
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 80.dp)
        )
    }
}

/**
 * 单个导航按钮
 *
 * @param isFirstButton 当前是否是第一个按钮被选中
 * @param text 按钮文本
 * @param modifier 把按钮放在合适的位置上
 */
@Composable
fun NavigationButton(isFirstButton: Boolean, text: String, modifier: Modifier) {
    val pressedColors =
        ButtonDefaults.buttonColors(disabledBackgroundColor = MaterialTheme.colors.primary)
    val notPressedColors = ButtonDefaults.buttonColors()
    Button(
        // 由于按钮不可点击所以回调为空
        onClick = { },
        // 按钮不可点击
        enabled = false,
        // 分布好看点
        modifier = modifier,
        // 圆形按钮
        shape = RoundedCornerShape(100),
        // 根据是否被选择来选择颜色
        colors = if (isFirstButton)
            pressedColors
        else notPressedColors
    ) {
        Text(text = text, fontSize = 22.sp)
    }
}