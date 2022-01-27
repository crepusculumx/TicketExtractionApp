/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

/**
 * 使用说明
 */
class HelpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                Scaffold(topBar = { TopBarWithTitleAndBack("使用说明") { finish() } }) {

                }
            }
        }
    }
}