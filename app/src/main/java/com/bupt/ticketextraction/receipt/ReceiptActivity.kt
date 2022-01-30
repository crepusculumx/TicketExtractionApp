/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.receipt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

/**
 * 展示发票信息的Activity
 */
@OptIn(ExperimentalMaterialApi::class)
class ReceiptActivity : ComponentActivity() {
    companion object {
        /**
         * Activity之间Intent传数据的extra名
         */
        const val TICKET_INTENT = "ticket"
    }

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
                    LazyColumn(Modifier.fillMaxWidth()) {
                        infoStr.forEach {
                            item { ReceiptInfoListItem(name = it.first, value = it.second) }
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
    ListItem(
        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
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