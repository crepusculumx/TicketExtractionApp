/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.bupt.ticketextraction.ui.compose.isInDarkTheme

private val DarkColorPalette = darkColors(
    primary = Gray13,
    primaryVariant = Gray9,
    secondary = Gray9,
    secondaryVariant = Gray8,
    background = Gray10,
    onBackground = Gray3
)

private val LightColorPalette = lightColors(
    primary = Sunset6,
    primaryVariant = Sunset5,
    secondary = Sunset5,
    secondaryVariant = Sunset4,
    background = Color.White,
    onBackground = Color.Black

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

/**
 * 根据当前主题(白天/夜间)选择颜色
 *
 * @param darkTheme 是否是夜间模式
 * @param content 内容
 */
@Composable
fun TicketExtractionTheme(
    darkTheme: Boolean = isInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}