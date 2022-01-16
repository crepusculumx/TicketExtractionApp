package edu.bupt.ticketextraction.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import edu.bupt.ticketextraction.ui.theme.TicketExtractionTheme

/**
 * 根据当前主题设置颜色
 */
@Composable
fun ActivityBody(content: @Composable () -> Unit) {
    // 获取当前主题
    val darkTheme = isInDarkTheme()
    TicketExtractionTheme(darkTheme = darkTheme) {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}