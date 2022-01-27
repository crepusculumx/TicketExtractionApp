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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.R
import com.bupt.ticketextraction.ui.compose.ActivityBody
import com.bupt.ticketextraction.ui.compose.TopBarWithTitleAndBack

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
                        val ch = Alignment.CenterHorizontally
                        // 用一个空的文本框占一下位
                        Text("", modifier = Modifier.size(5.dp))
                        Divider()

                        PersonInfoListItem("联系人管理", Modifier.align(ch)) {
                            // TODO: 2022/1/20 跳转到联系人
                        }
                        PersonInfoListItem("导出模板管理", Modifier.align(ch)) {
                            // TODO: 2022/1/20 跳转到模板
                        }

                        // 用一个空的文本框占一下位
                        Text("", modifier = Modifier.size(30.dp))
                        Divider()

                        PersonInfoListItem("修改密码", Modifier.align(ch)) {
                            // 跳转到ChangePassword
                            val intent = Intent(this@PersonInfoActivity, ChangePasswordActivity::class.java)
                            startActivity(intent)
                        }
                        PersonInfoListItem(text = "注销", Modifier.align(ch)) {
                            // 登录状态设为否，登录手机号设为空
                            LoginActivity.loginState = false
                            LoginActivity.curPhoneNumber = ""
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
private fun PersonInfoListItem(text: String, modifier: Modifier, onClick: () -> Unit) {
    ListItem(modifier = modifier.clickable { onClick() },
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