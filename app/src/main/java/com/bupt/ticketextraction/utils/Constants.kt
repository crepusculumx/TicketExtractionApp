/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * 定义需要初始化的常量
 */

/**
 * 初始化所有常量
 *
 * @param context 需要的上下文，本app的任意Activity都行
 */
fun initConst(context: Context) {
    EXTERNAL_FILE_DIR = context.getExternalFilesDir(null)!!.absolutePath
    TICKET_DATA = "$EXTERNAL_FILE_DIR/tickets.dat"
    LOGIN_DATA = "$EXTERNAL_FILE_DIR/login.dat"
    createFileIfNotExists(TICKET_DATA)
    createFileIfNotExists(LOGIN_DATA)
    IS_DEBUG_VERSION = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    val num = context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode
    CUR_VERSION_CODE = (num and 0x00000000FFFFFFFF).toInt()
}

/**
 * 外部存储目录
 */
var EXTERNAL_FILE_DIR = ""

/**
 * APK存储路径
 */
var APK_PATH = ""

/**
 * 存储票据信息
 */
var TICKET_DATA = ""

/**
 * 存储登录信息，7天内免登录
 */
var LOGIN_DATA = ""

/**
 * 是否是Debug版本
 */
var IS_DEBUG_VERSION = false

/**
 * 版本代码
 */
var CUR_VERSION_CODE = 0

/**
 * 手机号正则表达式
 */
val phoneNumberPattern: Pattern =
    Pattern.compile("(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}")

// 大写字母+数字+小写字母 3~16位
val passwordPattern: Pattern = Pattern.compile("[A-Za-z0-9]{6,16}")

/**
 * 邮箱正则表达式，可靠性未知XD
 */
val emailPattern: Pattern =
    Pattern.compile("^\\s*\\w+(?:\\.?[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$")

/**
 * 精确到秒的日期格式
 */
val secondDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)

/**
 * 最大联系人数
 */
const val MAX_CONTACT_CNT = 4