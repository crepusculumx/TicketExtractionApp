/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package edu.bupt.ticketextraction.receipt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import edu.bupt.ticketextraction.ui.compose.ActivityBody
import edu.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

/**
 * 展示发票信息的Activity
 */
class ReceiptActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                Scaffold(topBar = { TopBarWithTitleAndBack("发票信息") { finish() } }) {

                }
            }
        }
    }

}