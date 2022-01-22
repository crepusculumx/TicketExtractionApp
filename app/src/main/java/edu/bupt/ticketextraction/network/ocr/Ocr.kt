@file:Suppress("SENSELESS_COMPARISON")

package edu.bupt.ticketextraction.network.ocr

import android.util.Log
import edu.bupt.ticketextraction.utils.EXTERNAL_FILE_DIR
import edu.bupt.ticketextraction.utils.createFileIfNotExsits
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*


private val directory: File = File(EXTERNAL_FILE_DIR)
private val assessTokenFile = File("$directory/assess_token.dat")
private var accessToken = ""

/**
 * 调用ocr识别
 * TODO: 区分是图片还是视频，设置一个枚举类区分吧
 *
 * @param sourceFile 资源文件，图片或视频
 * @return 识别得到的发票信息
 */
fun extract(sourceFile: File) {
    val res = taxiReceipt(sourceFile)

    // 解析识别结果
    val result = JSONObject(res)
    var jsonObject = JSONObject(result.getJSONObject("words_result").toString())

}


private fun taxiReceipt(file: File): String {
    val url = "https://aip.baidubce.com/rest/2.0/ocr/v1/taxi_receipt"

    // 本地文件路径
    val filePath = file.absolutePath
    val imgData = FileUtil.readFileByBytes(filePath)
    val imgStr = Base64Util.encode(imgData)
    val imgParam = URLEncoder.encode(imgStr, "UTF-8")
    val param = "image=$imgParam"
    setAccessToken()
    return HttpUtil.post(url, accessToken, param)
}

/**
 * 获取API访问token
 * 该token有一定的有效期，需要自行管理，当失效时需重新获取.<br></br>
 * 使用synchronized是因为在启动时主线程会调用此函数，需要同步一下
 *
 * @return access_token
 */
@Synchronized
fun setAccessToken() {
    // 不存在文件时创建
    createFileIfNotExsits(assessTokenFile.absolutePath)

    var line: String
    val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)

    BufferedReader(InputStreamReader(FileInputStream(assessTokenFile))).use { reader ->
        // 读入access_token
        // 如果为空则说明这是第一次读取，从服务器获取access_token并缓存到本地
        line = reader.readLine()
        if (line == null) {
            Log.e("access_token", "post")
            accessToken = authFromBaidu
            return
        }
        accessToken = line

        // 能读到这说明文件不为空，日期一定存在
        // 读入assess_token获取的日期
        line = reader.readLine()
        val lastDate = format.parse(line)
        val curDate = Date()
        val diff = curDate.time - lastDate!!.time
        // 如果日期大于30天则获取新的
        if (diff / (24 * 60 * 60 * 1000) >= 30) {
            accessToken = authFromBaidu
            return
        }
    }

}


@Suppress("SpellCheckingInspection")
private val authFromBaidu: String
    get() {
        val ak = "3n6XC5aCjUq37vcKqZE1qgde"
        val sk = "ZcrDxscvXHLCawooVqz0xw9cA7x2EbsR"
        // 获取token地址
        val authHost = "https://aip.baidubce.com/oauth/2.0/token?"
        val getAccessTokenUrl = (authHost
                + "grant_type=client_credentials" // 1. grant_type为固定参数
                + "&client_id=" + ak // 2. 官网获取的 API Key
                + "&client_secret=" + sk) // 3. 官网获取的 Secret Key

        FileOutputStream(assessTokenFile).use { outputStream ->
            val realUrl = URL(getAccessTokenUrl)
            // 打开和URL之间的连接
            val connection = realUrl.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            // 定义 BufferedReader输入流来读取URL的响应
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val result = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                result.append(line)
            }
            /*
            * 返回结果
            */
            val jsonObject = JSONObject(result.toString())
            val res = jsonObject.getString("access_token").trimIndent()
            // 把结果缓存到文件 assess_token + date
            outputStream.write(res.toByteArray(StandardCharsets.UTF_8))
            outputStream.write(
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())
                    .toByteArray(StandardCharsets.UTF_8)
            )
            return res
        }

    }

/**
 * 当字符串为空串""时，返回识别异常
 *
 * @param str 要检查的字符串
 * @return 当字符串为空串""时，返回识别异常;否则返回字符串本身
 */
private fun requiresNotEmpty(str: String, defaultVal: String): String {
    return if (str == "") defaultVal else str
}

