/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
@file:Suppress("DuplicatedCode")

package com.bupt.ticketextraction.receipt

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

//filePath
//invoiceCode
//invoiceNumber
//taxiNum
//date
//time
//pickUpTime
//dropOffTime
//fare
//fuelOilSurcharge
//callServiceSurcharge
//totalFare
//location
//province
//city
//pricePerKm
//distance

/**
 * 把发票数据写入文件中
 */
fun ObjectOutputStream.writeTicket(ticket: CabTicket) {
    val sb = StringBuilder()
    sb.append(ticket.filePath).append(" ")
    sb.append(ticket.invoiceCode).append(" ")
    sb.append(ticket.invoiceNumber).append(" ")
    sb.append(ticket.taxiNum).append(" ")
    sb.append(ticket.date).append(" ")
    sb.append(ticket.time).append(" ")
    sb.append(ticket.pickUpTime).append(" ")
    sb.append(ticket.dropOffTime).append(" ")
    sb.append(ticket.fare).append(" ")
    sb.append(ticket.fuelOilSurcharge).append(" ")
    sb.append(ticket.callServiceSurcharge).append(" ")
    sb.append(ticket.totalFare).append(" ")
    sb.append(ticket.location).append(" ")
    sb.append(ticket.province).append(" ")
    sb.append(ticket.city).append(" ")
    sb.append(ticket.pricePerKm).append(" ")
    sb.append(ticket.distance).append("\n")
    this.write(sb.toString().toByteArray())
}

fun ObjectInputStream.readTicket(): CabTicket? {
    val line = BufferedReader(InputStreamReader(this)).readLine() ?: return null
    val fields = line.split(" ")
    return CabTicket(
        fields[0], fields[1], fields[2], fields[3], fields[4], fields[5],
        fields[6], fields[7], fields[8], fields[9], fields[10], fields[11], fields[12],
        fields[13], fields[14], fields[15], fields[16]
    )
}

/**
 * 出租车发票数据类，包含了出租车发票的所有数据
 */
data class CabTicket(
    val filePath: String? = "", // 对应发票图片存储路径
    var invoiceCode: String? = "", // 发票代码
    var invoiceNumber: String? = "", // 发票号码
    var taxiNum: String? = "", // 出租车牌号
    var date: String? = "", // 日期
    var time: String? = "", // 上下车时间
    var pickUpTime: String? = "", // 上车时间
    var dropOffTime: String? = "", // 下车时间
    var fare: String? = "", // 金额
    var fuelOilSurcharge: String? = "", // 燃油附加费
    var callServiceSurcharge: String? = "", // 叫车服务费
    var totalFare: String? = "", // 总金额
    var location: String? = "", // 开票城市
    var province: String? = "", // 省
    var city: String? = "", // 市
    var pricePerKm: String? = "", // 单价
    var distance: String? = "", // 里程
) : Parcelable, Comparable<CabTicket> {
    companion object CREATOR : Parcelable.Creator<CabTicket> {
        override fun createFromParcel(parcel: Parcel): CabTicket {
            return CabTicket(parcel)
        }

        override fun newArray(size: Int): Array<CabTicket?> {
            return arrayOfNulls(size)
        }
    }

    private val map = HashMap<String, String>(9)

    init {
        map["发票代码"] = invoiceCode!!
        map["发票号码"] = invoiceNumber!!
        map["出租车牌号"] = taxiNum!!
        map["日期"] = date!!
        map["上下车时间"] = time!!
        map["开票城市"] = location!!
        map["单价"] = pricePerKm!!
        map["里程"] = distance!!
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(filePath)
        parcel.writeString(invoiceCode)
        parcel.writeString(invoiceNumber)
        parcel.writeString(taxiNum)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(pickUpTime)
        parcel.writeString(dropOffTime)
        parcel.writeString(fare)
        parcel.writeString(fuelOilSurcharge)
        parcel.writeString(callServiceSurcharge)
        parcel.writeString(totalFare)
        parcel.writeString(location)
        parcel.writeString(province)
        parcel.writeString(city)
        parcel.writeString(pricePerKm)
        parcel.writeString(distance)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun compareTo(other: CabTicket): Int {
        // 以时间作为主参数比较大小，时间越近越大
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            val lhs = format.parse(this.date!!)
            val rhs = format.parse(other.date!!)
            (lhs!!.time - rhs!!.time).toInt()
        } catch (e: ParseException) {
            // 注意有可能会有识别异常的情况，此时parse会出异常
            // 让识别异常的位于最顶端
            Log.e("Compare", "parse")
            1
        }
    }

    fun getFieldByName(name: String): String? {
        return map[name]
    }
}
