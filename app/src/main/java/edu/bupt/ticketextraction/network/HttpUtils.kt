/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
@file:Suppress("HttpUrlsUsage", "unused")

package edu.bupt.ticketextraction.network

import android.util.Log
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
private const val GET_CONTACT_URL = "$SERVER_URL/getMails"
private const val SET_CONTACT_URL = "$SERVER_URL/setMails"
private const val GET_VERSION_NUM = "$SERVER_URL/checkVersion"
private const val DOWNLOAD_APK = "$SERVER_URL/TaxiReceiptAPK"

/**
 * 调用登录服务
 *
 * @param phoneNumber 账号
 * @param password    密码
 * @return 1-登录成功, 0-密码错误, -1-用户名不存在
 */
suspend fun login(phoneNumber: String, password: String): Int {
    val cipherText = passwordEncrypt(password)
    val map = HashMap<String, String>()
    map["phone"] = phoneNumber
    map["key"] = cipherText
    return post(LOGIN_URL, map).toInt()
}

/**
 * 调用注册服务
 *
 * @param phoneNumber      账号
 * @param password         密码
 * @return 1-成功 -1-该用户名已被占用 -2-程序运行错误
 */
suspend fun register(phoneNumber: String, password: String): Int {
    val cipherText = passwordEncrypt(password)
    val map = HashMap<String, String>()
    map["phone"] = phoneNumber
    map["key"] = cipherText
    return post(REGISTER_URL, map).toInt()
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
    if (s == null) {
        Log.e("server", "no connection")
        return "ERROR"
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

    if (s == null) {
        // null因为猪头把服务器给关了
        Log.e("server", "no connection")
        return "ERROR"
    }
    return s.toString()
}