/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.receipt

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.R
import com.bupt.ticketextraction.main.MainActivity
import com.bupt.ticketextraction.settings.isLatestVersion
import com.bupt.ticketextraction.ui.compose.ProgressDialog
import com.bupt.ticketextraction.ui.compose.UpdateDialog
import com.bupt.ticketextraction.utils.DebugCode
import com.bupt.ticketextraction.utils.IS_DEBUG_VERSION
import com.bupt.ticketextraction.utils.toBeImplemented
import java.io.File
import java.io.IOException

/**
 * 保存所有发票信息
 */
@DebugCode
val tickets = if (IS_DEBUG_VERSION) {
    mutableStateListOf(
        // 测试用例
        CabTicket(
            invoiceCode = "123",
            invoiceNumber = "456",
            totalFare = "114514",
            distance = "66.0",
            date = "2022-01-19"
        ),
        CabTicket(
            invoiceCode = "123",
            invoiceNumber = "456",
            totalFare = "114514",
            distance = "66.0",
            date = "2022-01-19"
        ),
        CabTicket(
            invoiceCode = "123",
            invoiceNumber = "456",
            totalFare = "114514",
            distance = "66.0",
            date = "2022-01-19"
        )
    )
} else mutableStateListOf<CabTicket>()

var isOcrDialogShow = mutableStateOf(false)

/**
 * 写在外边是因为，这个弹窗，只需要弹出一次，写在外边是不会被销毁又重新构造的
 */
private var isUpdateShow = mutableStateOf(!isLatestVersion.value)

/**
 * 降序添加元素
 *
 * @param e 待添加元素
 */
fun <E : Comparable<E>> SnapshotStateList<E>.addDescendingOrder(e: E) {
    var index = 0
    // 找到应该添加的位置
    this.forEachIndexed { i, _ ->
        if (this[i] <= e) {
            return@forEachIndexed
        }
        ++index
    }
    this.add(index, e)
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ReceiptUI(fatherActivity: MainActivity) {
    // 为此页面加入滚动条，票据肯定会很多，需要上下拉页面
    LazyColumn(Modifier.fillMaxWidth()) {
        // 创建每一个发票对应的缩略展示
        tickets.forEach {
            item { ReceiptListItem(it, fatherActivity) }
        }
    }
    if (isOcrDialogShow.value) ProgressDialog("正在识别中...") { isOcrDialogShow.value = false }
    if (isUpdateShow.value) UpdateDialog(fatherActivity) { isUpdateShow.value = false }
}

/**
 * 从MainActivity跳转到ReceiptActivity
 *
 * @param ticket 跳转到的票据
 * @receiver MainActivity
 */
fun MainActivity.jumpToReceipt(ticket: CabTicket) {
    val intent = Intent(this, ReceiptActivity::class.java)
    ReceiptActivity.curTicket = ticket
    startActivity(intent)
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
    var expanded by remember { mutableStateOf(false) }
    ListItem(
        // 长按可以删除、验真，点击则跳转到详细信息
        modifier = Modifier.combinedClickable(onLongClick = {
            /* TODO 2022/1/15 验真 */
            expanded = true
        }) {
            fatherActivity.jumpToReceipt(ticket)
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
        Column {
            Text(
                text = "里程   ${ticket.distance}       总费用   ${ticket.totalFare}",
                fontSize = 19.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                ReceiptDropDownMenuItem("删除") {
                    try {
                        // 根据路径删除本地文件
                        File(ticket.filePath!!).delete()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    // 删除UI内的票据
                    tickets.remove(ticket)
                    Toast.makeText(fatherActivity, "删除成功", Toast.LENGTH_SHORT).show()
                    expanded = false
                }
                ReceiptDropDownMenuItem("验真") {
                    expanded = false
                    toBeImplemented(fatherActivity)
                }
            }
        }
    }

    // 来一条分隔线
    Divider()
}

/**
 * 点击单个票据时的下拉菜单的单个选项
 *
 * @param text 选项的文本
 * @param onClick 点击回调
 */
@Composable
private inline fun ReceiptDropDownMenuItem(text: String, crossinline onClick: () -> Unit) {
    DropdownMenuItem(onClick = { onClick() }) {
        Text(text)
    }
}