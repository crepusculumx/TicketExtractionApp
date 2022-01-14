package edu.bupt.ticketextraction.main

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
import edu.bupt.ticketextraction.ui.theme.Sunset5
import edu.bupt.ticketextraction.ui.theme.TicketExtractionTheme


class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicketExtractionTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navControllers = rememberNavController()
                    Scaffold(
                        // 顶部栏
                        topBar = {
                            MainTopBar()
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
                                composable(MainBottomNavItem.Settings.route) { SettingsUI() }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainTopBar() {
    TopAppBar(
        title = { Text("发票识别") },
        actions = {
            var expanded by remember { mutableStateOf(false) }
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Text("test")
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
        items.forEachIndexed { index, screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(screen.resId),
                        contentDescription = null
                    )
                },
                modifier = Modifier.padding(
                    start = screen.startPadding,
                    end = screen.endPadding
                ),
                label = { Text(screen.route) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navControllers.navigate(screen.route) {
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
        backgroundColor = Sunset5
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
