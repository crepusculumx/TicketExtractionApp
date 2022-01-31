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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

/**
 * 展示导出模板的Activity，TODO 同时也能编辑？
 */
class EmailTemplateActivity : ComponentActivity() {
    companion object {
        const val TEMPLATE_INTENT = "template"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 断言不为空
        val template = intent.getParcelableExtra<EmailTemplate>(TEMPLATE_INTENT)!!
        setContent {
            ActivityBody {
                Scaffold(topBar = { TopBarWithTitleAndBack(template.name) { finish() } }) { }
                template.items.forEachIndexed { index, it ->
                    TemplateItem(index, it.string)
                }
            }
        }
    }
}

@Composable
private fun TemplateItem(index: Int, field: String) {
    ListItem { Text("第${index + 1}列属性:  $field", fontSize = 20.sp) }
    Divider()
}