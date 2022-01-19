/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package edu.bupt.ticketextraction.receipt

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.bupt.ticketextraction.R
import edu.bupt.ticketextraction.main.MainActivity

/**
 * 保存所有发票信息
 */
val tickets = mutableListOf<CabTicket>()

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ReceiptUI(fatherActivity: MainActivity) {
    // 为此页面加入滚动条，票据肯定会很多，需要上下拉页面
    val scrollState = rememberScrollState()
    // 测试用例
    val ticket = CabTicket(
        invoiceCode = "123",
        invoiceNumber = "456",
        totalFare = "114514",
        distance = "66.0",
        date = "2022.1.19"
    )
    tickets.add(ticket)
    Column(
        Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
    ) {
        // 创建每一个发票对应的缩略展示
        tickets.forEach {
            ReceiptListItem(it, fatherActivity)
        }
    }
}

/**
 * 出租车发票缩略信息展示，包括缩略图、里程、日期和总费用
 *
 * @param ticket 出租车发票
 * @param fatherActivity 父Activity
 */
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
private fun ReceiptListItem(ticket: CabTicket, fatherActivity: MainActivity) {
    ListItem(
        // 长按可以删除、验真，点击则跳转到详细信息
        modifier = Modifier.combinedClickable(onLongClick = {/* TODO 2022/1/15 删除 验真 */ }) {
            fatherActivity.jumpFromMainToReceipt(ticket)
        },
        icon = {
            // TODO: 2022/1/15 把发票缩略图放在这里
            Icon(
                painterResource(id = R.drawable.ic_baseline_receipt_24),
                contentDescription = null
            )
        },
        // 第二文本放日期
        secondaryText = {
            Text(
                text = "日期   ${ticket.date}",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    ) {
        // 主文本放里程和总费用
        Text(
            text = "里程   ${ticket.distance}       总费用   ${ticket.totalFare}",
            fontSize = 19.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
    // 来一条分隔线
    Divider()
}