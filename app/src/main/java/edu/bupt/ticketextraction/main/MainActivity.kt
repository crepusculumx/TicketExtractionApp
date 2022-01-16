package edu.bupt.ticketextraction.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.bupt.ticketextraction.R
import edu.bupt.ticketextraction.compose.ActivityBody
import edu.bupt.ticketextraction.compose.TopBarText
import edu.bupt.ticketextraction.compose.changeTheme
import edu.bupt.ticketextraction.compose.isInDarkTheme

class MainActivity : ComponentActivity() {
    fun jumpFromMainToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                val navControllers = rememberNavController()
                Scaffold(
                    // 顶部栏
                    topBar = {
                        MainTopBar(isInDarkTheme()) { changeTheme() }
                    },
                    // 底部栏
                    bottomBar = {
                        MainBottomBar(navControllers = navControllers)
                    },
                    // 底部栏的圆形浮动按钮
                    floatingActionButton = {
                        MainFloatingActionButton()
                    },
                    // FAB嵌入到底部导航栏中
                    isFloatingActionButtonDocked = true,
                    // FAB位置在底部导航栏中间
                    floatingActionButtonPosition = FabPosition.Center,
                    content = {
                        NavHost(
                            navControllers,
                            startDestination = MainBottomNavItem.Receipt.route
                        ) {
                            composable(MainBottomNavItem.Receipt.route) { ReceiptUI() }
                            composable(MainBottomNavItem.Settings.route) { SettingsUI(this@MainActivity) }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MainTopBar(darkTheme: Boolean = false, darkThemeOnClick: () -> Unit) {
    TopAppBar(
        title = {
            TopBarText(isMain = true)
        },
        actions = {
            var expanded by remember { mutableStateOf(false) }
            // Button再跟DropdownMenu即可创建下拉菜单
            IconButton(onClick = { expanded = true }) {
                // 三个点竖直排列的图标
                Icon(Icons.Filled.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // 根据当前主题展示不同的文本和图片
                val dayOrNight = if (darkTheme) "白天" else "夜间"
                val resId = if (darkTheme) R.drawable.ic_baseline_wb_sunny_24
                else R.drawable.ic_baseline_brightness_3_24
                MainTopMoreDropdownMenuItem(resId = resId, text = "${dayOrNight}模式") {
                    darkThemeOnClick()
                }
                MainTopMoreDropdownMenuItem(resId = R.drawable.ic_baseline_email_24, text = "导出") {
                    // TODO: 2022/1/15
                }
                MainTopMoreDropdownMenuItem(resId = R.drawable.ic_baseline_help_24, text = "使用说明") {
                    // TODO: 2022/1/15
                }
            }
        }
    )
}

@Composable
fun MainBottomBar(navControllers: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(MainBottomNavItem.Receipt, MainBottomNavItem.Settings)
    BottomAppBar(cutoutShape = RoundedCornerShape(80.dp)) {
        items.forEachIndexed { index, it ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(it.resId),
                        contentDescription = null
                    )
                },
                modifier = Modifier.padding(
                    start = it.startPadding,
                    end = it.endPadding
                ),
                label = { Text(it.route) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navControllers.navigate(it.route) {
                        // 跳转到起始页面，即发票页面
                        popUpTo(navControllers.graph.startDestinationId)
                        // 一次只能展示一个页面
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun MainFloatingActionButton() {
    FloatingActionButton(
        onClick = {
            // TODO: 2022/1/14 调用相机
        },
        Modifier.size(80.dp),
    ) {
        Icon(
            painterResource(id = R.drawable.ic_outline_photo_camera_24),
            contentDescription = null,
            modifier = Modifier.size(50.dp),
            // 图片的颜色设置为白色
            tint = Color.White
        )
    }
}
