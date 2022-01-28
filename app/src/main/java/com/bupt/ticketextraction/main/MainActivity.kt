/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */

package com.bupt.ticketextraction.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.bupt.ticketextraction.R
import com.bupt.ticketextraction.email.EmailActivity
import com.bupt.ticketextraction.network.getVersionCode
import com.bupt.ticketextraction.network.ocr.setAccessToken
import com.bupt.ticketextraction.receipt.*
import com.bupt.ticketextraction.settings.SettingsUI
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.ui.compose.TopBarText
import com.bupt.ticketextraction.ui.compose.changeTheme
import com.bupt.ticketextraction.ui.compose.isInDarkTheme
import com.bupt.ticketextraction.utils.EXTERNAL_FILE_DIR
import com.bupt.ticketextraction.utils.TICKET_DATA
import com.bupt.ticketextraction.utils.createFileIfNotExists
import kotlinx.coroutines.*
import java.io.*

/**
 * APP根Activity
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity(), CoroutineScope by MainScope() {
    /**
     * 相机，用于拍照录视频
     */
    private val camera = Camera(this)

    /**
     * 从MainActivity跳转到EmailActivity
     */
    fun jumpFromMainToEmail() {
        val intent = Intent(this, EmailActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启动协程获取access_token
        launch {
            setAccessToken()
        }
        // 启动协程读取票据信息
        launch {
            readTickets()
        }
        // 启动协程检查更新
        launch {
            checkUpdate()
        }
        setContent {
            ActivityBody {
                val navControllers = rememberNavController()
                Scaffold(
                    // 顶部栏
                    topBar = {
                        MainTopBar(this, isInDarkTheme()) { changeTheme() }
                    },
                    // 底部栏
                    bottomBar = {
                        MainBottomBar(navControllers = navControllers)
                    },
                    // 底部栏的圆形浮动按钮
                    floatingActionButton = {
                        MainFloatingActionButton(camera)
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
                            composable(MainBottomNavItem.Receipt.route) { ReceiptUI(this@MainActivity) }
                            composable(MainBottomNavItem.Settings.route) { SettingsUI(this@MainActivity) }
                        }
                    }
                )
            }
        }
        initVars()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 把票据信息保存到文件里
        ObjectOutputStream(FileOutputStream(TICKET_DATA)).use {
            tickets.forEach { cabTicket ->
                it.writeTicket(cabTicket)
            }
        }
        // 如果不清空在应用未完全结束时再次启动就会出现多个相同的票据
        tickets.clear()
        // 在MainActivity生命周期结束时销毁所有协程
        cancel()
    }

    /**
     * 初始化一些共享变量
     */
    private fun initVars() {
        EXTERNAL_FILE_DIR = getExternalFilesDir(null)!!.absolutePath
        TICKET_DATA = "$EXTERNAL_FILE_DIR/tickets.dat"
    }

    /**
     * 读取所有票据信息
     */
    private suspend fun readTickets() {
        createFileIfNotExists(TICKET_DATA)
        // 读取文件的IO操作
        withContext(Dispatchers.IO) {
            try {
                val oos = ObjectInputStream(FileInputStream(TICKET_DATA))
                var ticket: CabTicket?
                while (oos.readTicket().also { ticket = it } != null) {
                    tickets.addDescendingOrder(ticket!!)
                }
            } catch (e: EOFException) {
                // 第一次创建时，必定会遇到EOF问题
                Log.e("main resume", "ticket data eof")
            }
        }
    }

    private suspend fun checkUpdate() {
        withContext(Dispatchers.IO) {
            val num = packageManager.getPackageInfo(packageName, 0).longVersionCode
            val versionCode = (num and 0x00000000FFFFFFFF).toInt()
            val latest = getVersionCode()
            if (versionCode < latest) {
                // TODO: 2022/1/27
                Log.i("update", "$latest")
            }
        }
    }
}

/**
 * MainActivity的TopBar
 *
 * @param fatherActivity 父Activity
 * @param darkTheme 当前是否是夜间模式
 * @param darkThemeOnClick 模式切换点击回调
 */
@Composable
private fun MainTopBar(
    fatherActivity: MainActivity,
    darkTheme: Boolean = false,
    darkThemeOnClick: () -> Unit
) {
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
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                // 根据当前主题展示不同的文本和图片
                val dayOrNight = if (darkTheme) "白天" else "夜间"
                val resId = if (darkTheme) R.drawable.ic_baseline_wb_sunny_24
                else R.drawable.ic_baseline_brightness_3_24
                MainTopMoreDropdownMenuItem(resId = resId, text = "${dayOrNight}模式") {
                    darkThemeOnClick()
                }
                MainTopMoreDropdownMenuItem(resId = R.drawable.ic_baseline_email_24, text = "导出") {
                    fatherActivity.jumpFromMainToEmail()
                }
                MainTopMoreDropdownMenuItem(resId = R.drawable.ic_baseline_help_24, text = "使用说明") {
                    // TODO: 2022/1/15
                }
            }
        }
    )
}

/**
 * MainActivity的BottomBar
 *
 * @param navControllers 导航Controller
 */
@Composable
private fun MainBottomBar(navControllers: NavHostController) {
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

/**
 * MainActivity的悬浮按钮，用于拍照
 */
@Composable
private fun MainFloatingActionButton(camera: Camera) {
    FloatingActionButton(
        onClick = {
            camera.captureImage()
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

/**
 * MainActivity的TopBar的更多按钮的DropdownMenu的Item，
 * 具备点击事件，一个图标 + 一个文本
 */
@Composable
private fun MainTopMoreDropdownMenuItem(resId: Int, text: String, onClick: () -> Unit) {
    DropdownMenuItem(onClick = { onClick() }) {
        Icon(
            painterResource(id = resId),
            contentDescription = null,
            // 原尺寸(24dp)太大，改为20dp再设置5dp的距离
            modifier = Modifier
                .size(20.dp)
                .padding(end = 5.dp)
        )
        Text(text = text)
    }
}