/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
@file:OptIn(ExperimentalMaterialApi::class)

package com.bupt.ticketextraction.email

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bupt.ticketextraction.R
import com.bupt.ticketextraction.ui.compose.RoundedCornerButton
import com.bupt.ticketextraction.ui.compose.TwoStepsActivity
import com.bupt.ticketextraction.ui.compose.isInDarkTheme
import com.bupt.ticketextraction.ui.theme.Gray3
import com.bupt.ticketextraction.ui.theme.Gray9
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * 发送邮件前选择邮箱和模板的Activity
 */
class SendEmailActivity : TwoStepsActivity(), CoroutineScope by MainScope() {
    private var emailAddress = mutableStateOf("")

    init {
        title = "发送邮件"
    }

    @Preview
    @Composable
    override fun naviItem1() {
        val ch = Alignment.CenterHorizontally
        Column(Modifier.fillMaxWidth().height(500.dp).padding(top = 50.dp)) {
            TextField(
                value = emailAddress.value,
                onValueChange = { emailAddress.value = it },
                // TextField设置居中
                modifier = Modifier.align(ch),
                // 方形账户图案
                leadingIcon = {
                    Icon(painterResource(R.drawable.ic_baseline_email_24), contentDescription = null)
                },
                // 一行内展示
                singleLine = true,
                // 无输入时的提示文本
                placeholder = { Text("请输入邮箱地址") },
                // 输入类型设为数字
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                // 背景色设为白色
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    textColor = MaterialTheme.colors.onBackground
                )
            )
            val scrollState = rememberScrollState()
            // 这里放联系人
            val bkgColor = if (isInDarkTheme()) Gray9 else Gray3
            Column(
                Modifier.width(276.dp).verticalScroll(scrollState).align(ch)
                    .background(color = bkgColor)
            ) {
                ContactListItem("武连增", "1228393790@qq.com")
            }
            RoundedCornerButton(
                text = "下一步", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 40.dp)
                    .size(width = 150.dp, height = 100.dp)
            ) {
                // 导航到2页面去，即选择模板
                navController.navigate("2")
                isFirstButton.value = false
            }
        }
    }

    @Composable
    override fun naviItem2() {
        val (selectedOption, onOptionSelected) = remember { mutableStateOf("") }
        Column(Modifier.selectableGroup()) {

        }
    }

    @Composable
    override fun content() {
//        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    @Composable
    private fun ColumnScope.ContactListItem(name: String, email: String) {
        ListItem(
            modifier = Modifier.align(Alignment.CenterHorizontally).clickable { emailAddress.value = email },
            secondaryText = { Text(email) }) {
            Text(name)
        }
        Divider()
    }
}