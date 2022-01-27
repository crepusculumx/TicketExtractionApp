/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package com.bupt.ticketextraction.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bupt.ticketextraction.network.ocr.extract
import com.bupt.ticketextraction.receipt.addDescendingOrder
import com.bupt.ticketextraction.receipt.dialogIsShow
import com.bupt.ticketextraction.receipt.tickets
import com.bupt.ticketextraction.utils.EXTERNAL_FILE_DIR
import com.bupt.ticketextraction.utils.createFileIfNotExists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val START_CAMERA = 1

/**
 * 相机类，可以拍照录视频
 */
class Camera(private val fatherActivity: MainActivity) {
    /**
     * 启动器必须先注册
     */
    private val pictureLauncher = fatherActivity.registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            Log.e("camera", "picture")

            fatherActivity.launch {
                withContext(Dispatchers.IO) {
                    // 压缩图片，也是IO操作，一起放进来
                    bitmapCompress(curImageFile!!)
                    // 识别图片
                    val res = extract(curImageFile!!)
                    tickets.addDescendingOrder(res)
                    dialogIsShow.value = false
                }
            }
            // 弹出识别中窗口
            dialogIsShow.value = true
        } else {
            // 失败的话就删除图片文件
            curImageFile!!.delete()
        }
    }

    /**
     * 当前图片文件
     */
    private var curImageFile: File? = null

    /**
     * 启动相机捕获照片
     */
    fun captureImage() {
        requestCameraPermission { startTakePicture() }
    }

    /**
     * 启动相机捕获视频
     */
    @Suppress("unused")
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
                createFileIfNotExists("${EXTERNAL_FILE_DIR}/images/IMG_${createTimeStamp()}.jpg")
            val uri: Uri = FileProvider.getUriForFile(
                fatherActivity,
                "edu.bupt.ticketextraction.FileProvider",
                imageFile
            )
            curImageFile = imageFile
            pictureLauncher.launch(uri)
        }
    }

    /**
     * 启动相机，拍照或录视频
     */
    private fun startVideo() {
        fatherActivity.packageManager?.let {
            // 创建文件
            val videoFile =
                createFileIfNotExists("${EXTERNAL_FILE_DIR}/videos/VIDEO_${createTimeStamp()}.mp4")
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

    /**
     * 生成时间戳
     *
     * @return 时间戳
     */
    @Suppress("NOTHING_TO_INLINE")
    private inline fun createTimeStamp(): String {
        return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())
    }

    /**
     * 压缩图片
     *
     * @param file 被压缩图片文件
     */
    private fun bitmapCompress(file: File) {
        // 用文件获取uri
        val uri = Uri.fromFile(file)
        //用uri获取Bitmap
        @Suppress("DEPRECATION")
        var photoBitMap = MediaStore.Images.Media.getBitmap(fatherActivity.contentResolver, uri)

        // 压缩质量
        var quality = 100
        val maxFileSize = 2000 // 2MB

        val byteArrayOutputStream = ByteArrayOutputStream()
        // 先质量压缩
        // 图片大小大于2M就循环压缩直到小于2M
        do {
            byteArrayOutputStream.reset()
            // 压缩图片
            photoBitMap!!.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
            try {
                FileOutputStream(file).use { fileOutputStream ->
                    // 覆盖之前的图片
                    fileOutputStream.write(byteArrayOutputStream.toByteArray())
                    fileOutputStream.flush()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            quality -= 10
        } while (byteArrayOutputStream.toByteArray().size / 1024 > maxFileSize && quality > 0)
        Log.e("image size", (byteArrayOutputStream.toByteArray().size / 1024).toString())

        // 再尺寸压缩
        val options = BitmapFactory.Options()
        // 只读长度进内存
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        options.inJustDecodeBounds = false
        val width = options.outWidth
        val height = options.outHeight
        val maxWidth = 2048
        val maxHeight = 2048
        var scale = 1 // 1表示不缩放

        if (width > height && width > maxWidth) {
            scale = width / maxWidth
        } else if (width <= height && height > maxHeight) {
            scale = height / maxHeight
        }
        options.inSampleSize = scale
        // 根据options进行尺寸缩放
        photoBitMap = BitmapFactory.decodeFile(file.absolutePath, options)
        Log.e("image scale", scale.toString())
        byteArrayOutputStream.reset()
        photoBitMap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        try {
            FileOutputStream(file).use { fileOutputStream ->
                // 覆盖之前的图片
                fileOutputStream.write(byteArrayOutputStream.toByteArray())
                fileOutputStream.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}