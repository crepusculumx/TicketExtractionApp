/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

/**
 * 关于我们Activity，展示一些开发团队的信息
 */
class AboutUsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                Scaffold(topBar = { TopBarWithTitleAndBack("关于我们") { finish() } }) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp)) {
                        AboutUsText(text = "本app为北京邮电大学创新创业训练项目")
                        AboutUsText(text = "团队成员：")
                        AboutUsText(text = "武连增、伍昶旭、杨灿、肖皓月")
                        Divider(modifier = Modifier.padding(bottom = 10.dp))
                        AboutUsText(text = "信息反馈邮箱：")
                        AboutUsText(text = "wulianzeng@bupt.edu.cn")
                    }
                }
            }
        }
    }
}

/**
 * 关于我们的文本
 *
 * @param text 文本字符串
 */
@Composable
private fun AboutUsText(text: String) {
    Text(
        text = text,
        fontSize = 21.sp,
        modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
    )
}