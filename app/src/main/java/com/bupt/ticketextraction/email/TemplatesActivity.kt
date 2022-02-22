/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.email

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.R
import com.bupt.ticketextraction.network.addTemplate
import com.bupt.ticketextraction.network.deleteTemplate
import com.bupt.ticketextraction.settings.templates
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * 展示所有模板缩略信息的Activity
 */
@OptIn(ExperimentalMaterialApi::class)
class TemplatesActivity : ComponentActivity(), CoroutineScope by MainScope() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                var isAddDialogShow by remember { mutableStateOf(false) }
                var isDeleteDialogShow by remember { mutableStateOf(false) }
                // 用于删除模板
                var curIndex = -1
                Scaffold(
                    topBar = { TopBarWithTitleAndBack("模板管理") { finish() } },
                    floatingActionButton = {
                        // 新增模板
                        FloatingActionButton(onClick = { isAddDialogShow = true }, Modifier.padding(bottom = 50.dp)) {
                            // 加号图片
                            Icon(Icons.Filled.Add, contentDescription = null)
                        }
                    }) {
                    // 加个滚动条，防止模板过多
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        templates.forEachIndexed { index, it ->
                            item {
                                // 长按删除，点击进入相应的展示具体信息的Activity
                                ListItem(Modifier.padding(bottom = 5.dp, top = 5.dp)
                                    .combinedClickable(onLongClick = {
                                        // 设置当前下标
                                        curIndex = index
                                        Log.e("delete", "cur$curIndex $index")
                                        isDeleteDialogShow = true
                                    }) {
                                        // 设置展示的模板
                                        TemplateActivity.curTemplate = it
                                        startActivity(Intent(this@TemplatesActivity, TemplateActivity::class.java))
                                    }
                                ) {
                                    Text(it.name, fontSize = 22.sp)
                                }
                                Divider()
                            }
                        }
                    }
                    // 添加模板弹窗
                    if (isAddDialogShow) {
                        var name by remember { mutableStateOf("") }
                        AlertDialog(
                            onDismissRequest = { isAddDialogShow = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    // 以name作为模板名添加模板
                                    val t = EmailTemplate(name)
                                    // 数据库添加
                                    launch { addTemplate(t) }
                                    // 本地添加
                                    templates.add(t)
                                    isAddDialogShow = false
                                }) { Text("新建") }
                            },
                            dismissButton = { TextButton(onClick = { isAddDialogShow = false }) { Text("取消") } },
                            text = {
                                TextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    leadingIcon = {
                                        Icon(
                                            painterResource(R.drawable.ic_baseline_format_list_numbered_24),
                                            contentDescription = null
                                        )
                                    },
                                    placeholder = { Text("请输入模板名") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    // 背景色设为白色
                                    colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background)
                                )
                            }
                        )
                    }

                    // 删除模板弹窗
                    if (isDeleteDialogShow) {
                        AlertDialog(onDismissRequest = { isDeleteDialogShow = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    // 删除选中模板
                                    // 数据库删除
                                    val t = templates[curIndex]
                                    launch { deleteTemplate(t) }
                                    // 本地删除
                                    templates.removeAt(curIndex)
                                    isDeleteDialogShow = false
                                }) { Text("删除") }
                            },
                            dismissButton = { TextButton(onClick = { isDeleteDialogShow = false }) { Text("取消") } },
                            title = { Text("是否删除此模板？") }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}