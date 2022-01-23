/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package edu.bupt.ticketextraction.utils

import java.io.File
import java.io.FileOutputStream

/**
 * 根据文件路径创建文件
 *
 * @param path 文件路径
 */
fun createFileIfNotExists(path: String, init: String? = null): File {
    val index = path.indexOfLast { it == '/' }
    assert(index != -1)
    // 先解析出文件的目录
    val dir = File(path.substring(0, index))
    // 不存在目录则创建
    if (!dir.exists()) {
        dir.mkdirs()
    }
    // 创建文件对象
    val file = File(path)
    // 不存在则创建，存在则直接返回对象
    if (!file.exists()) {
        file.createNewFile()
        FileOutputStream(file).use { fos ->
            fos.write(init?.let { init.toByteArray() })
            fos.flush()
        }
    }
    return file
}