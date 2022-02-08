/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
@file:Suppress("HttpUrlsUsage", "unused")

package com.bupt.ticketextraction.network

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.bupt.ticketextraction.settings.LoginActivity.Companion.curPhoneNumber
import com.bupt.ticketextraction.utils.APK_PATH
import com.bupt.ticketextraction.utils.DebugCode
import com.bupt.ticketextraction.utils.EXTERNAL_FILE_DIR
import com.bupt.ticketextraction.utils.IS_DEBUG_VERSION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

/**
 * 所有API的url
 */
private const val SERVER_URL = "http://ubuntu@crepusculumx.icu:8888"
private const val SEND_EMAIL_URL = "$SERVER_URL/mail"
private const val LOGIN_URL = "$SERVER_URL/login"
private const val REGISTER_URL = "$SERVER_URL/register"
private const val CHANGE_PWD_URL = "$SERVER_URL/setKey"
private const val GET_CONTACT_URL = "$SERVER_URL/getMails"
private const val SET_CONTACT_URL = "$SERVER_URL/setMails"
private const val GET_VERSION_CODE = "$SERVER_URL/checkVersion"
private const val DOWNLOAD_APK = "$SERVER_URL/TaxiReceiptAPK"

private const val NO_CONNECTION = "369"

/**
 * 调用登录服务
 *
 * @param phoneNumber 账号
 * @param password    密码
 * @return 1-登录成功, 0-密码错误, -1-用户名不存在, 369-网络错误
 */
suspend fun login(phoneNumber: String, password: String): Int {
    val cipherText = passwordEncrypt(password)
    val map = HashMap<String, String>()
    map["phone"] = phoneNumber
    map["key"] = cipherText
    @DebugCode
    return if (IS_DEBUG_VERSION) 1 else post(LOGIN_URL, map).toInt()
}

/**
 * 调用注册服务
 *
 * @param phoneNumber      账号
 * @param password         密码
 * @return 1-成功 -1-该用户名已被占用 -2-程序运行错误, 369-网络错误
 */
suspend fun register(phoneNumber: String, password: String): Int {
    val cipherText = passwordEncrypt(password)
    val map = HashMap<String, String>()
    map["phone"] = phoneNumber
    map["key"] = cipherText
    @DebugCode
    return if (IS_DEBUG_VERSION) 1 else post(REGISTER_URL, map).toInt()
}

/**
 * 修改密码服务
 *
 * @param password 密码
 * @return 1-成功 -1查无此人 -2程序运行错误
 */
suspend fun changePwd(password: String): Int {
    val cipherText = passwordEncrypt(password)
    val map = HashMap<String, String>()
    map["phone"] = curPhoneNumber
    map["key"] = cipherText
    @DebugCode
    return if (IS_DEBUG_VERSION) 1 else post(CHANGE_PWD_URL, map).toInt()
}

/**
 * 获取最新版本码
 *
 * @return 最新版本码
 */
suspend fun getLatestVersionCode(): Int {
    @DebugCode
    return if (IS_DEBUG_VERSION) 1 else get(GET_VERSION_CODE).toInt()
}

fun downloadApk(activity: ComponentActivity) {
    // 检查是否存在安装包，存在则直接安装
    val file = File(EXTERNAL_FILE_DIR + APK_PATH)
    if (file.exists()) {
        DownloadReceiver.install(activity, APK_PATH)
        return
    }
    val uri = Uri.parse(DOWNLOAD_APK)
    val dm = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val request = DownloadManager.Request(uri)
    request.setDestinationInExternalFilesDir(activity, null, APK_PATH)
    // 下载时和下载完成通知
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    request.setTitle("发票识别")
    request.setDescription("正在下载最新版app")
    // 指定文件类型为apk
    request.setMimeType("application/vnd.android.package-archive")
    dm.enqueue(request)

    Log.e("download", "start")
    Toast.makeText(activity, "正在下载中", Toast.LENGTH_SHORT).show()
    // 动态获取权限
    val hasInstallPermission: Boolean = activity.packageManager.canRequestPackageInstalls()
    if (!hasInstallPermission) {
        val pkgUri = Uri.parse("package:${activity.packageName}")
        val permissionIntent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, pkgUri)
        @Suppress("DEPRECATION")
        activity.startActivityForResult(permissionIntent, 1000)
    }
}

/**
 * 采用sha算法加密密码
 *
 * @param plainText 明文
 * @return 密文
 */
private fun passwordEncrypt(plainText: String): String {
    val messageDigest = MessageDigest.getInstance("sha")
    messageDigest.update(plainText.toByteArray())
    return BigInteger(messageDigest.digest()).toString(32)
}

/**
 * Post方法，传递参数获取结果
 *
 * @param urlStr url地址字符串
 * @param params post参数
 * @return 结果
 */
private suspend fun post(urlStr: String, params: Map<String, String>): String {
    var s: StringBuilder? = null

    try {
        withContext(Dispatchers.IO) {
            val url = URL(urlStr)
            // 耗时操作
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            //以下两行必须加否则报错.
            conn.doInput = true
            conn.doOutput = true
            val sb = StringBuilder()
            params.forEach { (key, value) ->
                sb.append(key).append("=").append(value).append("&")
            }
            // 删掉最后一个&
            sb.deleteCharAt(sb.lastIndex)

            // 发送参数
            BufferedWriter(OutputStreamWriter(conn.outputStream)).use { writer ->
                writer.write(sb.toString().trimIndent())
                writer.flush()
            }

            // 获取结果
            if (conn.responseCode > 400) BufferedReader(InputStreamReader(conn.errorStream))
            else BufferedReader(InputStreamReader(conn.inputStream)).use { reader ->
                var line: String?
                s = StringBuilder()
                while (reader.readLine().also { line = it } != null) {
                    s?.append(line)
                }
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    // null因为猪头把服务器给关了
    if (s == null || s.toString() == "") {
        Log.e("server", "no connection")
        return NO_CONNECTION
    }
    return s.toString()
}

/**
 * Get方法，获取相应url的数据
 *
 * @param urlStr url地址字符串
 * @return 数据
 */
private suspend fun get(urlStr: String): String {
    var s: StringBuilder? = null
    try {
        withContext(Dispatchers.IO) {
            val url = URL(urlStr)
            val conn = url.openConnection() as HttpURLConnection
            var line: String?
            // 获取结果
            BufferedReader(InputStreamReader(conn.inputStream)).use { reader ->
                s = StringBuilder()
                while (reader.readLine().also { line = it } != null) {
                    s?.append(line)
                }
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    if (s == null || s.toString() == "") {
        // null因为猪头把服务器给关了
        Log.e("server", "no connection")
        return NO_CONNECTION
    }
    return s.toString()
}