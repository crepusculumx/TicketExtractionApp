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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.bupt.ticketextraction.email.EmailTemplate
import com.bupt.ticketextraction.email.templateItems
import com.bupt.ticketextraction.settings.Contact
import com.bupt.ticketextraction.settings.LoginActivity.Companion.curPhoneNumber
import com.bupt.ticketextraction.settings.contacts
import com.bupt.ticketextraction.settings.templates
import com.bupt.ticketextraction.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.*
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.ProtocolException
import java.net.URL
import java.security.MessageDigest

/**
 * 所有API的url
 */
private const val SERVER_URL = "http://crepusculumx.icu:8888"
private const val SEND_EMAIL_URL = "$SERVER_URL/mail"
private const val LOGIN_URL = "$SERVER_URL/login"
private const val REGISTER_URL = "$SERVER_URL/register"
private const val CHANGE_PWD_URL = "$SERVER_URL/setKey"
private const val GET_CONTACT_URL = "$SERVER_URL/getMails"
private const val SET_CONTACT_URL = "$SERVER_URL/setMails"
private const val GET_VERSION_CODE = "$SERVER_URL/checkVersion"
private const val DOWNLOAD_APK = "$SERVER_URL/TaxiReceiptAPK"
private const val ADD_TEMPLATE = "$SERVER_URL/addModel"
private const val DELETE_TEMPLATE = "$SERVER_URL/deleteModel"
private const val UPDATE_TEMPLATE = "$SERVER_URL/setModelOrder"
private const val GET_TEMPLATE = "$SERVER_URL/getModelOrder"
private const val GET_TEMPLATES = "$SERVER_URL/getUserModels"

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
    return post(LOGIN_URL, map).toInt()

//    if (IS_DEBUG_VERSION) 1 else
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

/**
 * 发送邮件
 *
 * @param map 由template的generateExcel生成的map
 * @return 1-成功，-1-失败，369-网络问题
 */
suspend fun sendEmail(map: Map<String, String>): Int {
    val res = post(SEND_EMAIL_URL, map)
    return if (res == "True") 1 else if (res == "False") -1 else 369
}

/**
 * 获取服务器的联系人并存入app中
 * 赋值将在函数内进行，不用返回值
 *
 * @return 1-获取成功，-2-获取失败，应该是后端的问题吧, 369-网络问题
 */
suspend fun getContact(): Int {
    val map = HashMap<String, String>()
    map["phone"] = curPhoneNumber
    val res = post(GET_CONTACT_URL, map)
    // 如果是-2则可能是后端遇到了问题
    if (res == "-2" || res == NO_CONNECTION) return res.toInt()
    @DebugCode
    if (IS_DEBUG_VERSION) Log.e("contacts", res)
    val jsonObject = JSONObject(res)
    val contactsFromServer = mutableStateListOf<Contact>()
    for (i in 1..4) {
        val name = jsonObject.getString("name$i")
        // 当为空串时说明没有数据了，直接停掉
        if (name == "") {
            contacts = contactsFromServer
            return 1
        }
        val email = jsonObject.getString("mail$i")
        contactsFromServer.add(Contact(name, email))
    }
    contacts = contactsFromServer
    return 1
}

/**
 * 在服务器上更新联系人信息
 *
 * @return 1-获取成功，-2-获取失败，应该是后端的问题吧, 369-网络问题
 */
suspend fun setContact(): Int {
    val map = HashMap<String, String>()
    map["phone"] = curPhoneNumber
    val size = contacts.size
    // 保存存在的联系人
    for (i in 1..size) {
        map["name$i"] = contacts[i - 1].name
        map["mail$i"] = contacts[i - 1].email
    }
    // 没用到的就设为空串
    for (i in size + 1..MAX_CONTACT_CNT) {
        map["name$i"] = ""
        map["mail$i"] = ""
    }
    val res = post(SET_CONTACT_URL, map)
    Log.e("set contact", res)
    return res.toInt()
}

/**
 * 添加模板
 *
 * @param template 模板
 * @return 1成功 -1该模板名已存在 -2程序运行错误
 */
suspend fun addTemplate(template: EmailTemplate): Int {
    return post(ADD_TEMPLATE, generateTemplateMap(template)).toInt()
}

/**
 * 更新模板
 *
 * @param template 模板
 * @return 1成功 -1该模板名不存在 -2程序运行错误
 */
suspend fun updateTemplate(template: EmailTemplate): Int {
    return post(UPDATE_TEMPLATE, generateTemplateMap(template)).toInt()
}

/**
 * 删除模板
 *
 * @param template 模板
 * @return 1成功 -1该模板名不存在 -2程序运行错误
 */
suspend fun deleteTemplate(template: EmailTemplate): Int {
    val map = generateTemplateMap(template)
    map.remove("order")
    return post(DELETE_TEMPLATE, map).toInt()
}

/**
 * 生成template对应的map，代码复用一下
 */
private fun generateTemplateMap(template: EmailTemplate): HashMap<String, String> {
    val map = HashMap<String, String>()
    map["phone"] = curPhoneNumber
    map["model_name"] = template.name
    val sb = StringBuilder()
    template.items.forEach {
        sb.append(templateItems.indexOf(it))
    }
    map["order"] = sb.toString()
    return map
}

suspend fun getTemplates(): Int {
    val map = HashMap<String, String>()
    map["phone"] = curPhoneNumber
    val res = post(GET_TEMPLATES, map)
    // 结果若为369则网络连接有问题
    if (res == "369" || res == "1") return res.toInt()
    // 生成json对象
    if (IS_DEBUG_VERSION) Log.i("templates", res)
    val jsonResult = JSONObject(res)
    val size = jsonResult.getInt("result_num")
    val templatesFromServer = SnapshotStateList<EmailTemplate>()
    for (i in 0 until size) {
        val templateJson = jsonResult.getJSONObject("$i")
        val name = templateJson.getString("name")
        val order = templateJson.getString("order")
        val t = EmailTemplate(name)
        templatesFromServer.add(t)
        // 添加模板的item
        order.forEach {
            t.items.add(templateItems[it.code - 48])
        }
    }
    templates = templatesFromServer
    // 成功返回1
    return 1
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
            Log.e("url", urlStr)
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
    } catch (e: ProtocolException) {
        @DebugCode
        // avd自带bug，最后一行读不到，给结果补上最后一行
        when (urlStr) {
            GET_CONTACT_URL -> s?.append("}")
            GET_TEMPLATES -> s?.append("}")
            SEND_EMAIL_URL -> s?.append("True")
            else -> s?.append("1")
        }
        Log.e("avd err", "unexpected end of stream")
        e.printStackTrace()
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