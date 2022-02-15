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
    constructor(name: String, vararg items: EmailTemplateItem) : this(name) {
        items.forEach {
            this.items.add(it)
        }
    }

    @IgnoredOnParcel
    val items = mutableListOf<EmailTemplateItem>()

    /**
     * 不重复地添加一个模板item
     *
     * @param item 模板的元素
     */
    fun addItem(item: EmailTemplateItem) {
        if (!items.contains(item)) {
            items.add(item)
        }
    }

    /**
     * 根据票据信息和导出模板生成对应的map，由后端转换为excel
     *
     * @param tickets 生成所需的票据
     * @param email 邮箱
     * @return 生成的map
     */
    fun generateExcel(tickets: MutableList<CabTicket>, email: String): Map<String, String> {
        val rowCnt = tickets.size + 1
        val map = HashMap<String, String>()
        map["mail"] = email
        map["0"] = rowCnt.toString()
        val sb = StringBuilder()
        sb.append("发票").append(" ")
        items.forEach {
            sb.append(it.string).append(" ")
        }
        // 删除最后一个空格
        sb.deleteCharAt(sb.lastIndex)
        map["1"] = sb.toString()
        for (i in 2..rowCnt) {
            val ticket = tickets[i - 2]
            // 清空之前的Builder
            sb.clear()
            sb.append("${i - 1}").append(" ")
            items.forEach {
                sb.append(ticket.getFieldByName(it.string)!!).append(" ")
            }
            sb.deleteCharAt(sb.lastIndex)
            map["$i"] = sb.toString()
        }
        return map
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

