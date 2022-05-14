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
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.network.getLatestVersionCode
import com.bupt.ticketextraction.network.getNetworkType
import com.bupt.ticketextraction.network.ocr.setAccessToken
import com.bupt.ticketextraction.receipt.readTickets
import com.bupt.ticketextraction.settings.LoginActivity
import com.bupt.ticketextraction.settings.isLatestVersion
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.utils.*
import kotlinx.coroutines.*
import java.io.*
import java.util.*

/**
 * 启动Activity，负责加载数据
 */
class StartActivity : ComponentActivity(), CoroutineScope by MainScope() {
    companion object {
        suspend fun checkUpdate() {
            withContext(Dispatchers.IO) {
                val latest = getLatestVersionCode()
                APK_PATH = "/apk/TicketExtraction$latest.apk"
                // 根据结果赋值
                isLatestVersion.value = false //latest == CUR_VERSION_CODE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                @DebugCode
                Box(Modifier.fillMaxWidth().fillMaxHeight()) {
                    Text("加载页面 TODO好看点", fontSize = 30.sp, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
        Toast.makeText(this, "正在启动中...", Toast.LENGTH_SHORT).show()

        // 初始化常量
        initConst(this)
        launch {
            // 检查网络
            when (getNetworkType()) {
                2 -> Toast.makeText(this@StartActivity, "正在使用移动数据", Toast.LENGTH_SHORT).show()

                369 -> {
                    Toast.makeText(this@StartActivity, "同步失败，请检查网络连接", Toast.LENGTH_SHORT).show()
                }
            }
            val token = async {
                // 获取access_token
                setAccessToken()
            }
            val read = async {
                // 读取本地数据
                readTickets()
                readLogin()
            }
            val update = async {
                // 检查更新
                checkUpdate()
            }
            // 全部完毕之后启动主页面
            token.await()
            read.await()
            update.await()
            delay(1000)
            val intent = Intent(this@StartActivity, MainActivity::class.java)

            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 结束协程
        cancel()
    }

    /**
     * 读取所有票据信息
     */
    private suspend fun readTickets() {
        // 读取文件的IO操作
        withContext(Dispatchers.IO) {
            try {
                val file = File(TICKET_DATA)
                file.readTickets()
            } catch (e: EOFException) {
                // 第一次创建然后读取时，可能会遇到EOF问题
                @DebugCode
                Log.e("main resume", "ticket data eof")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 读取登录信息，7天内免登录
     */
    private suspend fun readLogin() {
        // 读取文件的IO操作
        withContext(Dispatchers.IO) {
            try {
                BufferedReader(InputStreamReader(FileInputStream(LOGIN_DATA))).use {
                    // 无日期则结束
                    val line = it.readLine() ?: return@withContext
                    val lastDate = secondDateFormat.parse(line)
                    val curDate = Date()
                    // 7天内无需重复登录，自动登录
                    if (curDate.time - lastDate!!.time <= 7 * 24 * 60 * 60 * 1000) {
                        LoginActivity.login(it.readLine())
                    }
                }
            } catch (e: EOFException) {
                // 第一次创建然后读取时，可能会遇到EOF问题
                @DebugCode
                Log.e("login", "login data eof")
            }
        }
    }
}