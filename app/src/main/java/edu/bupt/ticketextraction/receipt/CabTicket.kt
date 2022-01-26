/**
 * 北京邮电大学创新创业训练项目——出租车发票识别
 *
 * author 武连增
 *
 * e-mail: wulianzeng@bupt.edu.cn
 */
package edu.bupt.ticketextraction.receipt

import android.os.Parcel
import android.os.Parcelable
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * 把发票数据写入文件中
 */
fun ObjectOutputStream.writeTicket(ticket: CabTicket) {
    val sb = StringBuilder()
    ticket.fields.forEach {
        sb.append(it).append(" ")
    }
    sb.deleteCharAt(sb.lastIndex).append("\n")
    this.write(sb.toString().toByteArray())
}

fun ObjectInputStream.readTicket(): CabTicket? {
    val line = BufferedReader(InputStreamReader(this)).readLine() ?: return null
    val fields = line.split(" ")
    val res = CabTicket(fields[0])
    for (i in 1 until fields.size) {
        res.fields = fields as ArrayList<String>
    }
    return res
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
) : Parcelable {
    companion object CREATOR : Parcelable.Creator<CabTicket> {
        override fun createFromParcel(parcel: Parcel): CabTicket {
            return CabTicket(parcel)
        }

        override fun newArray(size: Int): Array<CabTicket?> {
            return arrayOfNulls(size)
        }
    }

    var fields = ArrayList<String>(17)

    init {
        fields.add(filePath!!)
        fields.add(invoiceCode!!)
        fields.add(invoiceNumber!!)
        fields.add(taxiNum!!)
        fields.add(date!!)
        fields.add(time!!)
        fields.add(pickUpTime!!)
        fields.add(dropOffTime!!)
        fields.add(fare!!)
        fields.add(fuelOilSurcharge!!)
        fields.add(callServiceSurcharge!!)
        fields.add(totalFare!!)
        fields.add(location!!)
        fields.add(province!!)
        fields.add(city!!)
        fields.add(pricePerKm!!)
        fields.add(distance!!)
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
        fields.forEach {
            parcel.writeString(it)
        }
    }

    override fun describeContents(): Int {
        return 0
    }
}
