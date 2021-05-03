package pub.teanote.lunarcalendar

/**
 * @param num
 * 月数，正月=1,二月=2,...冬月=11,腊月=12，闰月的num与前一月相同
 * @param jd
 * 初一的儒略日
 * @param monthName
 * 月份名称：正月，二月
 * @param isLeap
 * 闰月=true
 */
internal data class LunarMonthData(var num :Int, var jd: Double, var monthName :String = "", var isLeap :Boolean = false)

data class SolarTermData(val name :String, val time :String)