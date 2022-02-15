/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
@file:OptIn(ExperimentalMaterialApi::class)

package com.bupt.ticketextraction.email

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupt.ticketextraction.R
import com.bupt.ticketextraction.network.sendEmail
import com.bupt.ticketextraction.receipt.CabTicket
import com.bupt.ticketextraction.settings.contacts
import com.bupt.ticketextraction.settings.templates
import com.bupt.ticketextraction.ui.compose.ProgressDialog
import com.bupt.ticketextraction.ui.compose.RoundedCornerButton
import com.bupt.ticketextraction.ui.compose.TwoStepsActivity
import com.bupt.ticketextraction.ui.compose.isInDarkTheme
import com.bupt.ticketextraction.ui.theme.Gray3
import com.bupt.ticketextraction.ui.theme.Gray9
import com.bupt.ticketextraction.utils.DebugCode
import com.bupt.ticketextraction.utils.IS_DEBUG_VERSION
import com.bupt.ticketextraction.utils.emailPattern
import kotlinx.coroutines.*

/**
 * 发送邮件前选择邮箱和模板的Activity
 */
class SendEmailActivity : TwoStepsActivity(), CoroutineScope by MainScope() {
    private var emailAddress = mutableStateOf("")
    private var isEmailValid = mutableStateOf(false)
    private var isDialogShow = mutableStateOf(false)

    companion object {
        var curTickets = mutableListOf<CabTicket>()
    }

    init {
        title = "发送邮件"
        naviButtonBottomPadding = 100.dp
    }

    @Preview
    @Composable
    override fun naviItem1() {
        val ch = Alignment.CenterHorizontally
        Column(Modifier.fillMaxWidth().padding(top = 20.dp)) {
            TextField(
                value = emailAddress.value,
                onValueChange = {
                    emailAddress.value = it
                    // 判断有效性
                    isEmailValid.value = emailPattern.matcher(it).matches()
                },
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
            // 这里放联系人
            val bkgColor = if (isInDarkTheme()) Gray9 else Gray3
            LazyColumn(Modifier.size(width = 276.dp, height = 224.dp).align(ch).background(color = bkgColor)) {
                @DebugCode
                if (IS_DEBUG_VERSION) {
                    contacts.forEach { item { ContactListItem(it.name, it.email) } }
                }
            }
            RoundedCornerButton(
                text = "下一步", enabled = isEmailValid.value, modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width = 150.dp, height = 90.dp)
            ) {
                // 导航到2页面去，即选择模板
                navController.navigate("2")
                isFirstButton.value = false
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun naviItem2() {
        val ch = Alignment.CenterHorizontally
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(templates[0].name) }
        var curIndex = 0
        val bkgColor = if (isInDarkTheme()) Gray9 else Gray3
        Column(
            Modifier.selectableGroup().width(276.dp)
                .padding(top = 50.dp)
        ) {
            Text("请选择导出模板", modifier = Modifier.align(ch), fontSize = 21.sp)
            LazyColumn(Modifier.height(224.dp)) {
                templates.forEachIndexed { index, t ->
                    item {
                        Row(
                            Modifier.fillMaxWidth().height(56.dp).align(ch).background(bkgColor)
                                .selectable(
                                    role = Role.RadioButton,
                                    selected = (t.name == selectedOption),
                                    onClick = {
                                        onOptionSelected(t.name)
                                        curIndex = index
                                    })
                        ) {
                            ListItem(trailing = {
                                RadioButton(
                                    selected = (t.name == selectedOption),
                                    onClick = null
                                )
                            }) {
                                Text(t.name, fontSize = 22.sp)
                            }
                        }
                        Divider()
                    }
                }
            }
            RoundedCornerButton("发送", modifier = Modifier.align(ch)) {
                // TODO: 2022/1/29
                val map = templates[curIndex].generateExcel(curTickets, emailAddress.value)
                launch {
                    val deferred = async { sendEmail(map) }
                    when (deferred.await()) {
                        1 -> {
                            Toast.makeText(this@SendEmailActivity, "发送成功", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        -1 -> Toast.makeText(this@SendEmailActivity, "未知错误", Toast.LENGTH_SHORT).show()
                        369 -> Toast.makeText(this@SendEmailActivity, "网络连接失败！", Toast.LENGTH_SHORT).show()
                    }
                    isDialogShow.value = false
                }
                isDialogShow.value = true
            }
        }
    }

    @Composable
    override fun content() {
        if (isDialogShow.value) ProgressDialog("正在发送中...") { isDialogShow.value = false }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    @Composable
    private fun ColumnScope.ContactListItem(name: String, email: String) {
        ListItem(
            modifier = Modifier.align(Alignment.CenterHorizontally).height(56.dp)
                .clickable {
                    emailAddress.value = email
                    // 判断有效性
                    isEmailValid.value = emailPattern.matcher(email).matches()
                },
            secondaryText = { Text(email) }) {
            Text(name)
        }
        Divider()
    }
}