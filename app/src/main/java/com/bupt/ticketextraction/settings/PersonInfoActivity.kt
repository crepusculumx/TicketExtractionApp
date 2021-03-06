/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.R
import com.bupt.ticketextraction.email.EmailTemplate
import com.bupt.ticketextraction.email.TemplatesActivity
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack
import com.bupt.ticketextraction.utils.DebugCode
import com.bupt.ticketextraction.utils.IS_DEBUG_VERSION
import com.bupt.ticketextraction.utils.LOGIN_DATA
import com.bupt.ticketextraction.utils.defaultTemplate
import java.io.FileOutputStream

/**
 * 保存联系人
 * key-name value-email
 */
@DebugCode
var contacts = if (IS_DEBUG_VERSION) {
    mutableStateListOf(
        Contact("武连增1", "1228393790@qq.com"),
        Contact("武连增2", "1228393790@qq.com"),
        Contact("武连增3", "1228393790@qq.com"),
        Contact("武连增4", "1228393790@qq.com"),
        Contact("武连增5", "1228393790@qq.com")
    )
} else mutableStateListOf<Contact>()

/**
 * 保存模板
 */
@DebugCode
var templates = if (IS_DEBUG_VERSION) {
    mutableStateListOf(
        defaultTemplate,
        EmailTemplate("Test1"),
    )
} else mutableStateListOf(defaultTemplate)

/**
 * 展示个人信息的Activity
 */
@OptIn(ExperimentalMaterialApi::class)
class PersonInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBody {
                Scaffold(topBar = { TopBarWithTitleAndBack("账号管理") { finish() } }) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // 用一个空的文本框占一下位
                        Text("", modifier = Modifier.size(5.dp))
                        Divider()

                        PersonInfoListItem("联系人管理") {
                            startActivity(Intent(this@PersonInfoActivity, ContactActivity::class.java))
                        }
                        PersonInfoListItem("导出模板管理") {
                            startActivity(Intent(this@PersonInfoActivity, TemplatesActivity::class.java))
                        }

                        // 用一个空的文本框占一下位
                        Text("", modifier = Modifier.size(30.dp))
                        Divider()

                        PersonInfoListItem("修改密码") {
                            // 跳转到ChangePassword
                            val intent = Intent(this@PersonInfoActivity, ChangePasswordActivity::class.java)
                            startActivity(intent)
                        }
                        PersonInfoListItem(text = "注销") {
                            // 登录状态设为否，登录手机号设为空，同时还要清楚文件的登录信息
                            LoginActivity.loginState = false
                            LoginActivity.curPhoneNumber = ""
                            FileOutputStream(LOGIN_DATA).use {
                                it.write("".toByteArray())
                                it.flush()
                            }
                            finish()
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun ColumnScope.PersonInfoListItem(text: String, onClick: () -> Unit) {
    ListItem(modifier = Modifier.align(Alignment.CenterHorizontally).clickable { onClick() },
        trailing = {
            // 尾部添加一个箭头图标
            Icon(
                painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24),
                contentDescription = null
            )
        }) {
        Text(text = text, modifier = Modifier.padding(start = 3.dp), fontSize = 20.sp)
    }
    // 华丽的分割线
    Divider(thickness = 1.dp)
}