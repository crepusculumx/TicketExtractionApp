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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

/**
 * 展示发票信息的Activity TODO 可修改？
 */
@OptIn(ExperimentalMaterialApi::class)
class ReceiptActivity : ComponentActivity() {
    companion object {
        var curTicket: CabTicket? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 发票信息
        // 展示9项内容，开票城市单独展示不放在数组里
//        val infoStr = listOf(
//            Pair("发票代码", invoiceCode),
//            Pair("发票号码", invoiceNumber),
//            Pair("费用", totalFare),
//            Pair("日期", date),
//            Pair("上下车时间", time),
//            Pair("出租车牌号", taxiNum),
//            Pair("单价", pricePerKm),
//            Pair("里程", distance),
//        )
        val isDialogShow = Array(9) { mutableStateOf(false) }
        setContent {
            ActivityBody {
                Scaffold(topBar = { TopBarWithTitleAndBack("发票信息") { finish() } }) {
                    LazyColumn(Modifier.fillMaxWidth()) {
                        item {
                            ReceiptInfoListItem(name = "发票代码", value = curTicket?.invoiceCode) {
                                isDialogShow[0].value = true
                            }
                            if (isDialogShow[0].value) NumberEditDialog(
                                field = curTicket?.invoiceCode,
                                fieldName = "发票代码",
                                onValueChange = { curTicket?.invoiceCode = it }
                            ) { isDialogShow[0].value = false }
                        }
                        item {
                            ReceiptInfoListItem(name = "发票号码", value = curTicket?.invoiceNumber) {
                                isDialogShow[1].value = true
                            }
                            if (isDialogShow[1].value) NumberEditDialog(
                                field = curTicket?.invoiceNumber,
                                fieldName = "发票号码",
                                onValueChange = { curTicket?.invoiceNumber = it }
                            ) { isDialogShow[1].value = false }
                        }
                        item {
                            ReceiptInfoListItem(name = "费用", value = curTicket?.totalFare) {
                                isDialogShow[2].value = true
                            }
                            if (isDialogShow[2].value) NumberEditDialog(
                                field = curTicket?.totalFare,
                                fieldName = "费用",
                                onValueChange = { curTicket?.totalFare = it }
                            ) { isDialogShow[2].value = false }
                        }
                        item {
                            ReceiptInfoListItem(name = "日期", value = curTicket?.date) {
                                isDialogShow[3].value = true
                            }
                            if (isDialogShow[3].value) NumberEditDialog(
                                field = curTicket?.date,
                                fieldName = "日期",
                                onValueChange = { curTicket?.date = it }
                            ) { isDialogShow[3].value = false }
                        }
                        item {
                            ReceiptInfoListItem(name = "上下车时间", value = curTicket?.time) {
                                isDialogShow[4].value = true
                            }
                            if (isDialogShow[4].value) NumberEditDialog(
                                field = curTicket?.time,
                                fieldName = "上下车时间",
                                onValueChange = { curTicket?.time = it }
                            ) { isDialogShow[4].value = false }
                        }
                        item {
                            ReceiptInfoListItem(name = "出租车牌号", value = curTicket?.taxiNum) {
                                isDialogShow[5].value = true
                            }
                            if (isDialogShow[5].value) NumberEditDialog(
                                field = curTicket?.taxiNum,
                                fieldName = "出租车牌号",
                                onValueChange = { curTicket?.taxiNum = it }
                            ) { isDialogShow[5].value = false }
                        }
                        item {
                            ReceiptInfoListItem(name = "单价", value = curTicket?.pricePerKm) {
                                isDialogShow[6].value = true
                            }
                            if (isDialogShow[6].value) NumberEditDialog(
                                field = curTicket?.pricePerKm,
                                fieldName = "单价",
                                onValueChange = { curTicket?.pricePerKm = it }
                            ) { isDialogShow[6].value = false }
                        }
                        item {
                            ReceiptInfoListItem(name = "里程", value = curTicket?.distance) {
                                isDialogShow[7].value = true
                            }
                            if (isDialogShow[7].value) NumberEditDialog(
                                field = curTicket?.distance,
                                fieldName = "里程",
                                onValueChange = { curTicket?.distance = it }
                            ) { isDialogShow[7].value = false }
                        }
                        item {
                            ReceiptInfoListItem(name = "开票城市", value = curTicket?.location) {
                                isDialogShow[8].value = true
                            }
                            if (isDialogShow[8].value) NumberEditDialog(
                                field = curTicket?.location,
                                fieldName = "开票城市",
                                isNum = false,
                                onValueChange = { curTicket?.location = it }
                            ) { isDialogShow[8].value = false }
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
private fun ReceiptInfoListItem(
    name: String,
    value: String?,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp).clickable { onClick() },
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

@Composable
private fun NumberEditDialog(
    field: String?,
    fieldName: String,
    onValueChange: (String) -> Unit,
    isNum: Boolean = true,
    onDismiss: () -> Unit
) {
    var editableField by remember { mutableStateOf(field) }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("修改${fieldName}属性", color = MaterialTheme.colors.onBackground) },
        confirmButton = {
            TextButton(onClick = {
                onValueChange(editableField!!)
                onDismiss()
            }) { Text("修改", color = MaterialTheme.colors.onBackground) }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(
                    "取消",
                    color = MaterialTheme.colors.onBackground
                )
            }
        },
        text = {
            TextField(
                value = editableField!!,
                onValueChange = { editableField = it },
                // 一行内展示
                singleLine = true,
                // 无输入时的提示文本
                placeholder = { Text("请输入$fieldName") },
                // 输入类型设为数字
                keyboardOptions = if (isNum) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                // 背景色设为白色
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    textColor = MaterialTheme.colors.onBackground
                )
            )

        }
    )
}