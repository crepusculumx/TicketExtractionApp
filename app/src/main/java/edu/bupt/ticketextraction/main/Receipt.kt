package edu.bupt.ticketextraction.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.bupt.ticketextraction.R

/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */

/**
 * 保存所有发票信息
 */
val tickets = mutableListOf<CabTicket>()

@ExperimentalMaterialApi
@Preview
@Composable
fun ReceiptUI() {
    // 为此页面加入滚动条，票据肯定会很多，需要上下拉页面
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
    ) {
        // 测试用例
        ListItem(
            modifier = Modifier.clickable { /* TODO 2022/1/15 */ },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_receipt_24),
                    contentDescription = null
                )
            },
            secondaryText = {
                Text(
                    text = "日期   2022-1-15",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
        ) {
            Text(
                text = "里程   1.3km       总费用   66元",
                fontSize = 19.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        Divider()

        // 创建每一个发票对应的缩略展示
        tickets.forEach {
            ReceiptListItem(it)
        }
    }

}

/**
 * 出租车发票缩略信息展示，包括缩略图、里程、日期和总费用
 *
 * @param ticket 出租车发票
 */
@ExperimentalMaterialApi
@Composable
fun ReceiptListItem(ticket: CabTicket) {
    ListItem(
        modifier = Modifier.clickable { /* TODO 2022/1/15 跳转到发票详细信息 */ },
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