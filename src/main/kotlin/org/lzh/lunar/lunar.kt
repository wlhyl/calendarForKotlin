package org.lzh.lunar

import jodd.time.JulianDate
import org.lzh.util.*
import org.lzh.ganzhiwuxing.*
import java.time.*

// Calendar 保存公历年内计算农历所需的信息
class Calendar(val Year: Int) {
    //    val Year = year            // 公历年份
    val SolarTermJDs = get25SolarTermJDs(Year - 1, DongZhi)   // 相关的 25 节气 UTC间 儒略日(力学时)
    val SolarTermTimes = Array(SolarTermJDs.size) {
        //为UTC时间，带有时区信息
        i: Int ->
        GetDateTimeFromJulianDay(SolarTermJDs[i])
    }
    val NewMoonJDs = get15NewMoonJDs() //UTC时间的儒略日力学时
    val Months = fillMonths()      // 月
    val solarTermYearDays = Array(12) {
        // 十二节的 在所在年的第几天，此处需要将时区转换为东八区，因为每个时区的新年对应的UTC时间不同
        i: Int ->
        SolarTermTimes[2 * i + 1].withZoneSameInstant(ZoneId.of("+08:00")).dayOfYear
//        SolarTermTimes[2 * i + 1].withZoneSameInstant(ZoneId.of("Asia/Shanghai")).dayOfYear
//        使用"+08:00"，防止使用"Asia/Shanghai"引起转换到上海当时区时，
//        java1.8中"Asia/Shanghai"会转换成东八区，python中会转换成上海当地时区
    }

    init {
        calcLeapMonth()
    }

    private fun fillMonths(): Array<Month> {
        //采用夏历建寅，冬至所在月份为农历11月
//        val months = Array(14, { Month(0, 0, 0.0, ZonedDateTime.now(), false) })
        val months: MutableList<Month> = mutableListOf()
        var yuejian = 11
        for (i in 0 until 14) {
            val month = Month(0, 0, 0.0, ZonedDateTime.now(), false)
            if (yuejian <= 12) {
                month.Number = yuejian
            } else {
                month.Number = yuejian - 12
            }
            month.ShuoJD = NewMoonJDs[i]
            month.ShuoTime = GetDateTimeFromJulianDay(month.ShuoJD)
            val nextShuoJD = NewMoonJDs[i + 1]
            // 应当以晚上11:00开始算作第二天，下式以晚上12:00作第二天，因晚上11:00的jd会有小数部分，
            // 精度不高，暂时以下式作计算
            //以晚11:00作第二日，应当加上24-11/24再取整
            // TODO
            month.Days = (nextShuoJD + 0.5).toInt() - (month.ShuoJD + 0.5).toInt()
//            month.Days = (nextShuoJD + 13.0/24).toInt() - (month.ShuoJD + 13.0/24).toInt()
            months.add(month)
            yuejian++
        }
        return months.toTypedArray()
    }

    private fun calcLeapMonth() {
        // 根据节气计算是否有闰月，如果有闰月，根据农历月命名规则，调整月名称
        if ((NewMoonJDs[13] + 0.5).toInt() <= (SolarTermJDs[24] + 0.5).toInt()) {
            // 第13月的月末没有超过冬至，说明今年需要闰一个月
            var i = 1
            while (i < 14) {
                if ((NewMoonJDs[i + 1] + 0.5).toInt() <= (SolarTermJDs[2 * i] + 0.5).toInt()) {
                    /* cc.NewMoonJDs[i + 1] 是第i个农历月的下一个月的月首
                       本该属于第i个月的中气如果比下一个月的月首还晚，或者与下个月的月首是同一天（民间历法），则说明第 i 个月没有中气, 是闰月 */
                    break
                }
                i++
            }
            if (i < 14) {
                // 找到闰月
                // fmt.Println("找到闰月 ", i)
                Months[i].IsLeap = true
                // 对后面的农历月调整月名
                while (i < 14) {
                    Months[i].Number--
                    i++
                }
            }
        }
    }

    private fun get15NewMoonJDs(): Array<Double> {
        // 计算从某个时间之后的连续15个朔日
        // 参数: jd 开始时间的 儒略日
        // 返回 15个朔日时间 数组指针 儒略日北京时间
        //力学时
//        var tmpNewMoonJD = getNewMoonJD(JDBeijingTime2UTC(SolarTermJDs[0]))
        var tmpNewMoonJD = getNewMoonJD(SolarTermJDs[0])
        while (tmpNewMoonJD > SolarTermJDs[0]) {
            tmpNewMoonJD -= 29.53
        }
        var jd = tmpNewMoonJD
        val list: MutableList<Double> = mutableListOf()
        for (i in 0 until 15) {
            val newMoonJD = getNewMoonJD(jd)
//            list.add(JDUTC2BeijingTime(newMoonJD))
            list.add(newMoonJD)
            // 转到下一个最接近朔日的时间
            jd = newMoonJD + 29.53
        }
        return list.toTypedArray()
    }

    // SolarDayToLunarDay 指定年份内公历日期转换为农历日
    fun SolarDayToLunarDay(month: Int, day: Int): Day {
//    由于要计算日期在所在年的天数，因此需要使用东八区时间
//        为方便UTC的计算，使用20:00的时间进行计算，这样UTC时间将是12:00，对应的JD是整数
//        val zoneID = ZoneId.of("Asia/Shanghai")
        val zoneID = ZoneId.of("+08:00")
        val dt = ZonedDateTime.of(Year, month, day, 20, 0, 0, 0, zoneID)
        val yd = dt.dayOfYear

        // 求月地支,0为子月，1为丑月
        var monthZhi = 0
        while (monthZhi < solarTermYearDays.size) {
            if (yd >= solarTermYearDays[monthZhi]) {
                monthZhi++
            } else {
                break
            }
        }

        // 求农历月份和日
        lateinit var lunarMonth: Month //= Month(0, 0, 0.0, ZonedDateTime.now(), false)
        var lunarDay = 0
        for (m in Months) {
//            将合朔时间归算到东八区的当日00:00，当时也归算到东八区的00:00
            var shuoTime = m.ShuoTime.withZoneSameInstant(zoneID)
            val dd = deltaDays(shuoTime.toLocalDate(), dt.toLocalDate()) + 1 //合朔当日即为1日，所以需要加1
            if (1 <= dd && dd <= m.Days) {
                lunarMonth = m
                lunarDay = dd
                break
            }
        }

        // 求二十四节气
        var solarTerm = -1
        val solarTermInfos = getMonthSolarTerms(month)
        if (day == solarTermInfos[0].Day) {
            solarTerm = solarTermInfos[0].SolarTerm
        } else if (day == solarTermInfos[1].Day) {
            solarTerm = solarTermInfos[1].SolarTerm
        }

        var y = Year
        if (month < 2) y = Year - 1//如果是农历还没到正月，以去年的年份表示
        return Day(y, lunarDay, lunarMonth, monthZhi + 1, solarTerm)
    }
    private fun getMonthSolarTerms(month: Int): Array<solarTermInfo> {
//    var list [2]*solarTermInfo
        val index = 2 * month - 1
        val list0 = getSolarTermInfo(index)
        val list1 = getSolarTermInfo(index + 1)
        return arrayOf(list0, list1)
    }

    private fun getSolarTermInfo(index: Int): solarTermInfo {
        val dt = SolarTermTimes[index]
        val day = dt.dayOfMonth
        val stIndex = (index + DongZhi) % 24
        return solarTermInfo(day, stIndex)
    }
}

// Month 保存农历月信息
data class Month(
        var Number: Int,       // 农历月名
        var Days: Int,       // 本月天数
        var ShuoJD: Double,   // 本月朔日时间 UTC时间 儒略日，力学时
        var ShuoTime: ZonedDateTime, // 本月朔日时间 UTC时间
        var IsLeap: Boolean      // 是否为闰月
)

fun get25SolarTermJDs(y: Int, s: Int): Array<Double> {
    // 从某一年的某个节气开始，连续计算25个节气，返回各节气的儒略日
    // year 年份
    // start 起始的节气
    // 返回 25 个节气的 儒略日UTC时间
    var year = y
    var start = s
    var stOrder = start
    val list: MutableList<Double> = mutableListOf()
    for (i in 0 until 25) {
        val jd = GetSolarTermJD(year, stOrder)
//        list.add(JDUTC2BeijingTime(jd))
        list.add(jd)
        if (stOrder == DongZhi) {
            year++
        }
        stOrder = (stOrder + 1) % 24
    }
    return list.toTypedArray()
}

fun deltaDays(t1: LocalDate, t2: LocalDate): Int {
    // 计算两个时间相差的天数
    // t2 > t1 结果为正数
    val date1 = JulianDate.of(t1)//.withZoneSameInstant(ZoneId.of("UTC")).toLocalDate())
    val date2 = JulianDate.of(t2)//.withZoneSameInstant(ZoneId.of("UTC")).toLocalDate())
//    dd := int((date2.Unix() - date1.Unix()) / 86400)
    val dd = date2.doubleValue() - date1.doubleValue()
    // fmt.Printf("%v <=> %v dd %v\n", t2, t1, dd)
    return dd.toInt()
}

data class solarTermInfo(
        val Day: Int, //节气所在号数
        val SolarTerm: Int //节气序数，冬至为0
)


// GetYearGanZhi 计算年份的干支
fun GetYearGanZhi(year: Int): GanZhi {
    return GanZhi(TianGan("甲"), DiZhi("子")) + ((year - 1864))
}

// GetDayGanZhi 计算日干支
fun GetDayGanZhi(year: Int, month: Int, day: Int): GanZhi {
//    unixTime := time.Date(int(year), time.Month(month), int(day),
//    0, 0, 0, 0, time.UTC).Unix()
//    dayCyclical := int(unixTime/86400) + 29219 + 18
//    return cyclical(dayCyclical)
    val t0 = JulianDate.of(1900, 6, 20, 12, 0, 0, 0)
    val t = JulianDate.of(year, month, day, 12, 0, 0, 0)
    val dd = t.julianDayNumber - t0.julianDayNumber

    return GanZhi(TianGan("甲"), DiZhi("子")) + dd
}