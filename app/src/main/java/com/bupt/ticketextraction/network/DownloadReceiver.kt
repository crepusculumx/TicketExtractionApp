/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.network

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import com.bupt.ticketextraction.utils.APK_PATH
import com.bupt.ticketextraction.utils.EXTERNAL_FILE_DIR
import java.io.File

/**
 * Download receiver
 *
 */
class DownloadReceiver : BroadcastReceiver() {
    companion object {
        /**
         * @param context 上下文
         * @param path    安装包路径
         */
        fun install(context: Context, path: String) {
            val installIntent = Intent(Intent.ACTION_VIEW)
            val fileUri = FileProvider.getUriForFile(context, "com.bupt.ticketextraction.FileProvider", File(path))
            Log.e("download", fileUri.path!!)
            installIntent.setDataAndType(fileUri, "application/vnd.android.package-archive")
            // 设置允许其他应用读文件，不然会解析包失败
            installIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // 启动安装
            context.startActivity(installIntent)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            installApk(context, id, EXTERNAL_FILE_DIR + APK_PATH)
        }
    }

    private fun installApk(context: Context, apkId: Long, path: String) {
        Log.e("download", "id: $apkId")
        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        // 这个uri是检查下载是否有问题
        val uri = dm.getUriForDownloadedFile(apkId)
        if (uri != null) {
            install(context, path)
        }
    }
}