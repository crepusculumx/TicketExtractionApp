/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package edu.bupt.ticketextraction.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 圆角带字的按钮
 *
 * @param text 按钮提示字符
 * @param modifier 主要是为了让按钮居中
 * @param onClick 按钮的点击事件回调
 */
@Composable
fun RoundedCornerButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .padding(top = 40.dp)
            .size(width = 100.dp, height = 50.dp),
        // 来个圆角按钮
        shape = RoundedCornerShape(50),
        // 按钮背景颜色和主题背景颜色区分开，所以使用secondary
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onBackground
        )
    ) {
        Text(text = text, fontSize = 20.sp)
    }
}