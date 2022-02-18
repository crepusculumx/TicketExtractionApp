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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.settings.templates
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

/**
 * 展示所有模板缩略信息的Activity
 */
@OptIn(ExperimentalMaterialApi::class)
class TemplatesActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                Scaffold(topBar = { TopBarWithTitleAndBack("模板") { finish() } },
                    floatingActionButton = {
                        // 新增模板
                        FloatingActionButton(
                            onClick = {
                                // TODO: 2022/2/18 弹出一个AlertDialog，里面编辑一下名字，然后生成一个新的空模板
                            }, modifier = Modifier.padding(bottom = 50.dp)
                        ) {
                            // 加号图片
                            Icon(Icons.Filled.Add, contentDescription = null)
                        }
                    }) {
                    // 加个滚动条，防止模板过多
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        templates.forEach {
                            item {
                                // 长按删除，点击进入相应的展示具体信息的Activity
                                ListItem(Modifier.padding(bottom = 5.dp, top = 5.dp)
                                    .combinedClickable(onLongClick = {
                                        // TODO: 2022/2/18 删除模板弹窗
                                    }) {
                                        // 设置展示的模板
                                        EmailTemplateActivity.curTemplate = it
                                        startActivity(
                                            Intent(
                                                this@TemplatesActivity,
                                                EmailTemplateActivity::class.java
                                            )
                                        )
                                    }
                                ) {
                                    Text(it.name, fontSize = 22.sp)
                                }
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}