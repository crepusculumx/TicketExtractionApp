/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package edu.bupt.ticketextraction.main

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import edu.bupt.ticketextraction.utils.EXTERNAL_FILE_DIR
import edu.bupt.ticketextraction.utils.createFileIfNotExsits
import java.text.SimpleDateFormat
import java.util.*

private const val START_CAMERA = 1

/**
 * 相机类，可以拍照录视频
 */
class Camera(private val fatherActivity: MainActivity) {
    /**
     * 启动相机捕获照片
     */
    fun captureImage() {
        requestCameraPermission { startTakePicture() }
    }

    /**
     * 启动相机捕获视频
     */
    fun captureVideo() {
        requestCameraPermission { startVideo() }
    }

    /**
     * 获取相机权限后执行回调
     *
     * @param block 拥有权限后执行的回调函数，拍照或录视频
     */
    private inline fun requestCameraPermission(block: () -> Unit) {
        // 获取是否拥有权限
        val checkCallPhonePermission =
            ContextCompat.checkSelfPermission(fatherActivity, Manifest.permission.CAMERA)
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            // 向用户获取拍照权限
            ActivityCompat.requestPermissions(
                fatherActivity,
                arrayOf(Manifest.permission.CAMERA),
                START_CAMERA
            )
        } else {
            // 有权限，执行对应回调，拍照或录制
            block()
        }
    }

    /**
     * 启动相机，拍照或录视频
     */
    private fun startTakePicture() {
        fatherActivity.packageManager?.let {
            // 创建文件
            val imageFile =
                createFileIfNotExsits("${EXTERNAL_FILE_DIR}/images/IMG_${createTimeStamp()}.jpg")
            val uri: Uri = FileProvider.getUriForFile(
                fatherActivity,
                "edu.bupt.ticketextraction.FileProvider",
                imageFile
            )
            fatherActivity.registerForActivityResult(ActivityResultContracts.TakePicture()) {
                if (it) {
                    // TODO: 2022/1/22 识别图片
                    Log.e("camera", "picture")
                } else {
                    // 失败的话就删除图片文件
                    imageFile.delete()
                }
            }.launch(uri)
        }
    }

    /**
     * 启动相机，拍照或录视频
     */
    private fun startVideo() {
        fatherActivity.packageManager?.let {
            // 创建文件
            val videoFile =
                createFileIfNotExsits("${EXTERNAL_FILE_DIR}/videos/VIDEO_${createTimeStamp()}.mp4")
            val uri: Uri = FileProvider.getUriForFile(
                fatherActivity,
                "edu.bupt.ticketextraction.FileProvider",
                videoFile
            )
            fatherActivity.registerForActivityResult(ActivityResultContracts.CaptureVideo()) {
                if (it) {
                    // TODO: 2022/1/22 识别视频
                    Log.e("camera", "video")
                } else {
                    // 失败的话就删除图片文件
                    videoFile.delete()
                }
            }.launch(uri)
        }
    }

    private fun createTimeStamp(): String {
        return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())
    }
}