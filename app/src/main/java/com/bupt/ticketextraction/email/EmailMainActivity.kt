/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.email

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.R
import com.bupt.ticketextraction.receipt.CabTicket
import com.bupt.ticketextraction.receipt.tickets
import com.bupt.ticketextraction.settings.LoginActivity
import com.bupt.ticketextraction.settings.LoginActivity.Companion.loginState
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

@OptIn(ExperimentalMaterialApi::class)
class EmailActivity : ComponentActivity() {
    /**
     * CheckBox是否checked的数组
     * 与tickets一一对应，若为true则添加该发票的信息到邮件中
     */
    private val checked = mutableListOf<MutableState<Boolean>>()

    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                Scaffold(
                    topBar = { TopBarWithTitleAndBack("导出") { finish() } }
                ) {
                    Box(modifier = Modifier.fillMaxHeight()) {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            for (index in tickets.indices) {
                                // 为CheckBox的checked赋值
                                checked.add(mutableStateOf(false))
                                item {
                                    // 导出用户选中的EmailListItem对应的发票
                                    CheckBoxListItem(
                                        ticket = tickets[index],
                                        checked = checked[index].value,
                                        onCheckChange = { checked[index].value = it }
                                    )
                                }
                            }
                        }
                        Row(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
                            val colors = MaterialTheme.colors
                            val colors1 = ButtonDefaults.buttonColors(
                                backgroundColor = colors.secondary,
                                contentColor = colors.onBackground
                            )
                            val colors2 = ButtonDefaults.buttonColors(
                                backgroundColor = colors.secondaryVariant,
                                contentColor = colors.onBackground
                            )
                            BottomButton(R.drawable.ic_baseline_delete_24, "删除", colors1) {

                            }
                            BottomButton(R.drawable.ic_baseline_email_24, "导出", colors2) {
                                // 必须先登录，不登录就弹出登录
                                val clazz = if (loginState) SendEmailActivity::class.java else LoginActivity::class.java
                                // 设置发送的票据
                                val sentTickets = mutableListOf<CabTicket>()
                                checked.forEachIndexed { index, it ->
                                    if (it.value) sentTickets.add(tickets[index])
                                }
                                SendEmailActivity.curTickets = sentTickets
                                startActivity(Intent(this@EmailActivity, clazz))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 展示带有CheckBox的ListItem，以便选中
 *
 * @param ticket 出租车发票
 * @param checked CheckBox是否被选中
 * @param onCheckChange checked更新回调
 */
@ExperimentalMaterialApi
@Composable
private fun CheckBoxListItem(ticket: CabTicket, checked: Boolean, onCheckChange: (Boolean) -> Unit) {
    ListItem(
        // 整个ListItem都可以被点击选中发送
        modifier = Modifier.toggleable(value = checked, onValueChange = onCheckChange),
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
        },
        // 尾部放CheckBox
        trailing = {
            Checkbox(
                checked = checked,
                // 在这里的onCheckedChange只能捕获尾部CheckBox
                // 区域的点击事件，太捞了，设置整个ListItem都可被点击
                onCheckedChange = null,
                // 太大了一行字排不开，小一点
                modifier = Modifier.size(30.dp)
            )
        }
    ) {
        // 主文本放里程和总费用
        Text(
            text = "里程   ${ticket.distance}       总费用   ${ticket.totalFare}",
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
    // 华丽分割线
    Divider()
}

/**
 * Email底部的功能按钮
 *
 * @param resId 图片资源ID
 * @param text 文本
 * @param onClick 点击回调
 * @receiver RowScope
 */
@Composable
private fun RowScope.BottomButton(resId: Int, text: String, colors: ButtonColors, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        // 按钮1:1，高50dp
        modifier = Modifier.weight(1F, true).height(50.dp),
        colors = colors
    ) {
        // 图片+文本
        Icon(painterResource(resId), contentDescription = null)
        Text(text, fontSize = 21.sp)
    }
}