/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package edu.bupt.ticketextraction.email

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.bupt.ticketextraction.R
import edu.bupt.ticketextraction.receipt.CabTicket
import edu.bupt.ticketextraction.receipt.tickets
import edu.bupt.ticketextraction.ui.compose.ActivityBody
import edu.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

class EmailActivity : ComponentActivity() {
    /**
     * CheckBox是否checked的数组
     * 与tickets一一对应，若为true则添加该发票的信息到邮件中
     */
    private val checked = mutableListOf<MutableState<Boolean>>()

    @SuppressLint("UnrememberedMutableState")
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                Scaffold(
                    topBar = { TopBarWithTitleAndBack("导出") { finish() } },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { /*TODO*/ }) {
                            // 加号图片
                            Icon(Icons.Filled.Add, contentDescription = null)
                        }
                    }
                ) {
                    for (index in tickets.indices) {
                        // 为CheckBox的checked赋值
                        checked.add(mutableStateOf(false))
                        // 导出用户选中的EmailListItem对应的发票
                        EmailListItem(
                            ticket = tickets[index],
                            checked = checked[index].value,
                            onCheckChange = { checked[index].value = it }
                        )
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
private fun EmailListItem(ticket: CabTicket, checked: Boolean, onCheckChange: (Boolean) -> Unit) {
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