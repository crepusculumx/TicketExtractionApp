/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package edu.bupt.ticketextraction.ui.compose

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import edu.bupt.ticketextraction.R

/**
 * 输入账号的TextField
 *
 * @param phoneNumber 编辑框修改的文本
 * @param modifier 用于让TextField居中，在单独的TextField中没有Modifier.align()
 * @param onValueChange 当输入服务更新文本时触发的回调，更新的文本作为回调的参数出现
 */
@Composable
fun PhoneNumberTextField(
    phoneNumber: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
) {
    // TODO: 2022/1/17 isError
    TextField(
        value = phoneNumber,
        onValueChange = { onValueChange(it) },
        // TextField设置居中
        modifier = modifier,
        // 方形账户图案
        leadingIcon = {
            Icon(Icons.Filled.AccountBox, contentDescription = null)
        },
        // 一行内展示
        singleLine = true,
        // 无输入时的提示文本
        placeholder = { Text("请输入手机号") },
        // 输入类型设为数字
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        // 背景色设为白色
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background,
            textColor = MaterialTheme.colors.onBackground
        )
    )
}

/**
 * 输入密码的TextField
 *
 * @param password 编辑框修改的文本
 * @param placeholder 没有文本时显示的提示
 * @param modifier 用于让TextField居中，在单独的TextField中没有Modifier.align()
 * @param onValueChange 当输入服务更新文本时触发的回调，更新的文本作为回调的参数出现
 */
@Composable
fun PasswordTextField(
    password: String,
    placeholder: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    TextField(
        // TODO: 2022/1/17 isError
        value = password,
        // 为了代码复用，必须把password传进来，又因为函数式编程，还得定义一个回调在外面修改ToT
        onValueChange = { onValueChange(it) },
        // TextField设置居中，必须得从Column或Row或者其他能设置的地方传进来，TextField Modifier没这个属性
        modifier = modifier,
        // 锁的图案，表示密码
        leadingIcon = {
            Icon(Icons.Filled.Lock, contentDescription = null)
        },
        // 控制密码是否隐藏
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                // 根据当前密码是否隐藏选择图片
                val resId =
                    if (passwordHidden)
                        R.drawable.ic_baseline_visibility_24
                    else R.drawable.ic_baseline_visibility_off_24
                // 根据当前密码是否隐藏选择提示
                val description =
                    if (passwordHidden) "展示密码" else "隐藏密码"
                Icon(
                    painterResource(id = resId),
                    contentDescription = description
                )
            }
        },
        // 一行内展示
        singleLine = true,
        // 无输入时的提示文本
        placeholder = { Text(placeholder) },
        // 密码是否隐藏
        visualTransformation = if (passwordHidden)
            PasswordVisualTransformation()
        else VisualTransformation.None,
        // 输入类型设为密码
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        // 背景色设为白色
        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background)
    )
}