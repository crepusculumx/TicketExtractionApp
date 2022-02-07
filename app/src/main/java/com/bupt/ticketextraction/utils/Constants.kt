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
 * 是否是Debug版本
 */
var IS_DEBUG_VERSION = false

/**
 * 版本代码
 */
var CUR_VERSION_CODE = 0