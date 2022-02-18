/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
@file:OptIn(ExperimentalMaterialApi::class)

package com.bupt.ticketextraction.email

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.settings.defaultTemplate
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

private var isDialogShow = mutableStateOf(false)

/**
 * 当前被选中的item的索引，用于删除
 */
private var curIndex = 0

/**
 * 展示导出模板的Activity，TODO 同时也能编辑？
 */
class EmailTemplateActivity : ComponentActivity() {
    companion object {
        var curTemplate = defaultTemplate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                Scaffold(topBar = { TopBarWithTitleAndBack(curTemplate.name) { finish() } },
                    floatingActionButton = {
                        // 新增模板元素
                        FloatingActionButton(
                            onClick = {
                                // 因为只有9个字段可以放上去，所以最多允许九个
                                if (curTemplate.items.size < templateItems.size) {
                                    curTemplate.items.add(EmailTemplateItem.InvoiceCode)
                                } else {
                                    Toast.makeText(this@EmailTemplateActivity, "最多只能有9个字段！", Toast.LENGTH_SHORT).show()
                                }
                            }, modifier = Modifier.padding(bottom = 50.dp)
                        ) {
                            // 加号图片
                            Icon(Icons.Filled.Add, contentDescription = null)
                        }
                    }) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // 不用foreach是因为foreach不能修改
                        for (i in 0 until curTemplate.items.size) {
                            TemplateItem(i, curTemplate.items[i].string) { curTemplate.items[i] = it }
                        }
                    }
                }
                if (isDialogShow.value) {
                    // 修改或新增AlertDialog
                    AlertDialog(
                        onDismissRequest = { isDialogShow.value = false },
                        title = { Text("是否要删除？") },
                        confirmButton = {
                            TextButton(onClick = {
                                curTemplate.items.removeAt(curIndex)
                                isDialogShow.value = false
                            }) { Text("删除") }
                        },
                        dismissButton = { TextButton(onClick = { isDialogShow.value = false }) { Text("取消") } }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun TemplateItem(index: Int, field: String, onClick: (EmailTemplateItem) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ListItem(modifier = Modifier.combinedClickable(onLongClick = {
        curIndex = index
        isDialogShow.value = true
    }) { expanded = true }) {
        Column {
            Text("第${index + 1}列属性:  $field", fontSize = 20.sp)
            // 用一个下拉菜单让用户选择
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                templateItems.forEach {
                    // 展示除自己之外的item
                    if (it.string != field) {
                        DropdownMenuItem(onClick = {
                            onClick(it)
                            // 点击完之后收起菜单
                            expanded = false
                        }) { Text(it.string) }
                    }
                }
            }
        }
    }
    Divider()
}