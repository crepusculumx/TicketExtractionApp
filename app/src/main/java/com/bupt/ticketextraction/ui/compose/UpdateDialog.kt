/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.ui.compose

import androidx.activity.ComponentActivity
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import com.bupt.ticketextraction.network.downloadApk

/**
 * 更新弹窗
 */
@Composable
fun UpdateDialog(fatherActivity: ComponentActivity, onDismiss: () -> Unit) {
    AlertDialog(
        title = { Text("存在最新版本，是否更新？", color = MaterialTheme.colors.onBackground) },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = {
                downloadApk(fatherActivity)
                onDismiss()
            }) { Text("更新", color = MaterialTheme.colors.onBackground) }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(
                    "取消",
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    )
}