/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.settings

import android.os.Bundle
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.ui.compose.EmailTextField
import com.bupt.ticketextraction.ui.compose.NameTextField
import com.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

/**
 * 展示联系人并提供增加、删除和修改
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
class ContactActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                var isDialogShow by remember { mutableStateOf(false) }
                var name by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                Scaffold(topBar = { TopBarWithTitleAndBack("联系人") { finish() } },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(bottom = 50.dp)) {
                            // 加号图片
                            Icon(Icons.Filled.Add, contentDescription = null)
                        }
                    }) {
                    Column(Modifier.fillMaxWidth()) {
                        // 二级文本放邮箱
                        // 主文本放姓名
                        // 长按删除 点击编辑
                        contacts.forEach {
                            ListItem(
                                Modifier.align(Alignment.CenterHorizontally).padding(bottom = 5.dp, top = 5.dp)
                                    .combinedClickable(onLongClick = {/*TODO: 2022/1/30 删除*/ }) {
                                        name = it.key
                                        email = it.value
                                        isDialogShow = true
                                    },
                                secondaryText = { Text(text = it.value, fontSize = 19.sp) }) {
                                Text(text = it.key, fontSize = 22.sp)
                            }
                            Divider()
                        }
                        if (isDialogShow) {
                            AlertDialog(
                                onDismissRequest = { isDialogShow = false },
                                title = { Text("编辑联系人") },
                                confirmButton = { TextButton(onClick = { isDialogShow = false }) { Text("修改") } },
                                dismissButton = { TextButton(onClick = { isDialogShow = false }) { Text("取消") } },
                                text = {
                                    Column {
                                        NameTextField(name) { name = it }
                                        EmailTextField(email) { email = it }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}