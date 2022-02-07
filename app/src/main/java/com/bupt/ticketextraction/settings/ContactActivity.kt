/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.settings

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
                var isChangeDialogShow by remember { mutableStateOf(false) }
                var isDeleteDialogShow by remember { mutableStateOf(false) }
                // TextField中展示的name
                var name by remember { mutableStateOf("") }
                // TextField中展示的email
                var email by remember { mutableStateOf("") }
                // 根据新建或修改展示confirm button的文本
                var confirmText = "修改"
                // 被修改的联系人
                var curContact: Contact? = null
                // 当前联系人要放入的数组索引
                var curIndex = -1
                Scaffold(
                    topBar = { TopBarWithTitleAndBack("联系人") { finish() } },
                    // 新增联系人按钮w
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                confirmText = "新增"
                                // 新增都展示为空字符串
                                // 当前联系人新建一个，索引是数组最后索引+1
                                name = ""
                                email = ""
                                curContact = Contact("", "")
                                curIndex = contacts.size
                                isChangeDialogShow = true
                            }, modifier = Modifier.padding(bottom = 50.dp)
                        ) {
                            // 加号图片
                            Icon(Icons.Filled.Add, contentDescription = null)
                        }
                    }) {
                    Column(Modifier.fillMaxWidth()) {
                        // 二级文本放邮箱
                        // 主文本放姓名
                        // 长按删除 点击编辑
                        contacts.forEachIndexed { index, it ->
                            ListItem(
                                Modifier.align(Alignment.CenterHorizontally).padding(bottom = 5.dp, top = 5.dp)
                                    .combinedClickable(onLongClick = {
                                        curIndex = index
                                        isDeleteDialogShow = true
                                    }) {
                                        confirmText = "修改"
                                        // 修改时自动把原name和email填充上
                                        name = it.name
                                        email = it.email
                                        // 设定当前联系人
                                        curContact = it
                                        // 设定当前索引
                                        curIndex = index
                                        isChangeDialogShow = true
                                    },
                                secondaryText = { Text(text = it.email, fontSize = 19.sp) }) {
                                Text(text = it.name, fontSize = 22.sp)
                            }
                            Divider()
                        }
                        if (isChangeDialogShow) {
                            // 修改或新增AlertDialog
                            AlertDialog(
                                onDismissRequest = { isChangeDialogShow = false },
                                title = { Text("${confirmText}联系人") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        // 根据当前信息修改联系人的属性
                                        curContact!!.name = name
                                        curContact!!.email = email

                                        // 如果是修改则删去原有的再添加一个修改后的
                                        // 如果是新增则直接在最后添加一个新的
                                        if (curIndex < contacts.size) contacts.removeAt(curIndex)
                                        contacts.add(curIndex, curContact!!)
                                        isChangeDialogShow = false
                                        Toast.makeText(this@ContactActivity, "${confirmText}成功", Toast.LENGTH_SHORT)
                                    }) { Text(confirmText) }
                                },
                                dismissButton = { TextButton(onClick = { isChangeDialogShow = false }) { Text("取消") } },
                                text = {
                                    Column {
                                        // 把名字和邮箱编辑框展示一下
                                        NameTextField(name) { name = it }
                                        EmailTextField(email) { email = it }
                                    }
                                }
                            )
                        }
                        // 删除AlertDialog
                        if (isDeleteDialogShow) {
                            AlertDialog(
                                onDismissRequest = { isDeleteDialogShow = false },
                                title = { Text("删除联系人") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        // 按索引删除
                                        contacts.removeAt(curIndex)
                                        isDeleteDialogShow = false
                                    }) { Text("删除") }
                                },
                                dismissButton = { TextButton(onClick = { isDeleteDialogShow = false }) { Text("取消") } }
                            )
                        }
                    }
                }
            }
        }
    }
}