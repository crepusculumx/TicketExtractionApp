package edu.bupt.ticketextraction

/**
 * 出租车发票数据类，包含了出租车发票的所有数据
 */
data class CabTicket(
    val filePath: String, // 对应发票图片存储路径
    var invoiceCode: String, // 发票代码
    var invoiceNumber: String, // 发票号码
    var taxiNum: String, // 出租车牌号
    var date: String, // 日期
    var time: String, // 上下车时间
    var pickUpTime: String, // 上车时间
    var dropOffTime: String, // 下车时间
    var fare: String, // 金额
    var fuelOilSurcharge: String, // 燃油附加费
    var callServiceSurcharge: String, // 叫车服务费
    var totalFare: String, // 总金额
    var location: String, // 开票城市
    var province: String, // 省
    var city: String, // 市
    var pricePerKm: String, // 单价
    var distance: String // 里程
)