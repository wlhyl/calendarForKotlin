package pub.teanote.lunarcalendar.calendar

import pub.teanote.ganzhiwuxing.DiZhi
import pub.teanote.ganzhiwuxing.GanZhi
import pub.teanote.lunarcalendar.lunar.*
import pub.teanote.lunarcalendar.util.getDateTimeFromJulianDay
import java.time.ZoneId
import java.time.LocalDateTime

/**
 * 给定日期的农历信息
 * 如：2021/4/2的农历信息
 * 包括：
 * 年干支
 * 月干支
 * 农历月数
 * 农历日数
 * 节：节名，节时刻
 * 气：气名，气时刻
 */
data class LunarDayInfo(
    val GanZhiYear: GanZhi,
    val GanZhiMonth: GanZhi,
    val GanZhiDay: GanZhi,
    val LunarMonthName: String,
    val LunarDayName: String,
    val LunarLeapMonth: Boolean,
//            Zodiac         string
    val SolarTermFist: solarTermInfo,
    val SolarTermSecond: solarTermInfo
//            SolarFestival  string
//            LunarFestival  string
//            Worktime       int32
)

/**
 * 传公历年，月，日，返回农历年
 */
fun solarToLunar(year: Int, month: Int, day: Int): LunarDayInfo {
    val cc = Calendar(year)
    val lunarDay = cc.solarDayToLunarDay(month, day)
    val monthZhi = lunarDay.GanZhiMonth.zhi
    var stOrder = when (monthZhi) {
        DiZhi("子") -> DaXue
        DiZhi("丑") -> XiaoHan
        DiZhi("寅") -> LiChun
        DiZhi("卯") -> JingZhe
        DiZhi("辰") -> QingMing
        DiZhi("巳") -> LiXia
        DiZhi("午") -> MangZhong
        DiZhi("未") -> XiaoShu
        DiZhi("申") -> LiQiu
        DiZhi("酉") -> BaiLu
        DiZhi("戌") -> HanLu
        else -> LiDong
    }
    val solarTerm0 = solarTermInfo(getSolarTermName(stOrder), getSolarTermTime(year, stOrder))
    val solarTerm1 = solarTermInfo(
        getSolarTermName((stOrder + 1) % 24),
        getSolarTermTime(year, (stOrder + 1) % 24)
    )
    val dayInfo = LunarDayInfo(
//            GetYearGanZhi(year),
            GetYearGanZhi(lunarDay.Year),
            lunarDay.GanZhiMonth,
            GetDayGanZhi(year, month, day),
            lunarDay.MonthName,
            lunarDay.DayName,
            lunarDay.Month.isLeap,
            solarTerm0,
            solarTerm1
//        lunarDay.SolarTermName,
//        SolarFestival:  solarDay.Festival(),
//        LunarFestival:  lunarDay.Festival(),
//        Zodiac:         lunar.GetYearZodiac(year),
    )
    return dayInfo
}

private fun getSolarTermTime(year: Int, stOrder: Int): LocalDateTime {
    val jd = getSolarTermJD(year, stOrder)
    val t = getDateTimeFromJulianDay(jd).withZoneSameInstant(ZoneId.of("+08:00")).toLocalDateTime()
//    val jd1=GetSolarTermJD(year, (stOrder+1)%24)
//    val t1 = GetDateTimeFromJulianDay(jd1).withZoneSameInstant(ZoneId.of("+08:00")).toLocalDateTime()
//    return arrayOf(t0,t1)
    return t
}

data class solarTermInfo(
        val name: String, //节气的名称
        val t: LocalDateTime //节气的北京时间
)