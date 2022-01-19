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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.bupt.ticketextraction.ui.compose.ActivityBody
import edu.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

/**
 * 展示发票信息的Activity
 */
class ReceiptActivity : ComponentActivity() {
    companion object {
        /**
         * Activity之间Intent传数据的extra名
         */
        const val TICKET_INTENT = "ticket"
    }

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 断言不为空
        val ticket = intent.getParcelableExtra<CabTicket>(TICKET_INTENT)!!
        // 发票信息
        // 展示8项内容
        val infoStr = listOf(
            Pair("发票代码", ticket.invoiceCode),
            Pair("发票号码", ticket.invoiceNumber),
            Pair("费用", ticket.totalFare),
            Pair("日期", ticket.date),
            Pair("上车时间", ticket.pickUpTime),
            Pair("下车时间", ticket.dropOffTime),
            Pair("单价", ticket.pricePerKm),
            Pair("里程", ticket.distance)
        )
        setContent {
            ActivityBody {
                Scaffold(topBar = { TopBarWithTitleAndBack("发票信息") { finish() } }) {
                    Column(Modifier.fillMaxWidth()) {
                        infoStr.forEach {
                            ReceiptInfoListItem(name = it.first, value = it.second)
                        }
                    }
                }
            }
        }
    }
}

/**
 * 发票信息详细展示
 */
@ExperimentalMaterialApi
@Composable
private fun ReceiptInfoListItem(name: String, value: String?) {
    // 添加滚动条
    val scrollState = rememberScrollState()
    ListItem(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .verticalScroll(scrollState),
        trailing = {
            // 发票属性值
            // value不可能也不应该为空
            Text(text = value!!, fontSize = 22.sp)
        }) {
        // 发票属性名
        Text(text = name, fontSize = 20.sp)
    }
    // 华丽分割线
    Divider()
}