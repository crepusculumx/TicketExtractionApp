/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
@file:Suppress("unused")

package com.bupt.ticketextraction.email

import android.os.Parcelable
import com.bupt.ticketextraction.receipt.CabTicket
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * 导出模板类
 */
@Parcelize
class EmailTemplate(val name: String) : Parcelable {
    @IgnoredOnParcel
    val items = ArrayList<EmailTemplateItem>()

    fun addItem(item: EmailTemplateItem) {
        if (!items.contains(item)) {
            items.add(item)
        }
    }

    fun generateExcel(tickets: ArrayList<CabTicket>) {

    }
}

sealed class EmailTemplateItem(val string: String) {
    object InvoiceCode : EmailTemplateItem("发票代码") // 发票代码
    object InvoiceNumber : EmailTemplateItem("发票号码") // 发票号码
    object TaxiNum : EmailTemplateItem("出租车牌号") // 出租车牌号
    object Date : EmailTemplateItem("日期") // 日期
    object Time : EmailTemplateItem("上下车时间") // 上下车时间
    private object PickUpTime : EmailTemplateItem("上车时间") // 上车时间
    private object DropOffTime : EmailTemplateItem("下车时间") // 下车时间
    private object Fare : EmailTemplateItem("金额") // 金额
    private object FuelOilSurcharge : EmailTemplateItem("燃油附加费") // 燃油附加费
    private object CallServiceSurcharge : EmailTemplateItem("叫车服务费") // 叫车服务费
    object TotalFare : EmailTemplateItem("总金额") // 总金额
    object Location : EmailTemplateItem("开票城市") // 开票城市
    private object Province : EmailTemplateItem("省") // 省
    private object City : EmailTemplateItem("市") // 市
    object PricePerKm : EmailTemplateItem("单价") // 单价
    object Distance : EmailTemplateItem("里程") // 里程
}

