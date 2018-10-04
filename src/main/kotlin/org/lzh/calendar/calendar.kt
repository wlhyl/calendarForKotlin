package org.lzh.calendar

import org.lzh.ganzhiwuxing.DiZhi
import org.lzh.ganzhiwuxing.GanZhi
import org.lzh.lunar.*
import org.lzh.util.GetDateTimeFromJulianDay
import java.time.ZoneId
import java.time.LocalDateTime

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

fun SolarToLunar(year: Int, month: Int, day: Int): LunarDayInfo {
//    solarDay := Day{
//        Year:  year,
//        Month: month,
//        Day:   day,
//    }
    val cc = Calendar(year)
    val lunarDay = cc.SolarDayToLunarDay(month, day)
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
    val solarTerm0 = solarTermInfo(GetSolarTermName(stOrder), getSolarTermTime(year, stOrder))
    val solarTerm1 = solarTermInfo(GetSolarTermName((stOrder + 1) % 24), getSolarTermTime(year, (stOrder + 1) % 24))
    val dayInfo = LunarDayInfo(
//            GetYearGanZhi(year),
            GetYearGanZhi(lunarDay.Year),
            lunarDay.GanZhiMonth,
            GetDayGanZhi(year, month, day),
            lunarDay.MonthName,
            lunarDay.DayName,
            lunarDay.Month.IsLeap,
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
    val jd = GetSolarTermJD(year, stOrder)
    val t = GetDateTimeFromJulianDay(jd).withZoneSameInstant(ZoneId.of("+08:00")).toLocalDateTime()
//    val jd1=GetSolarTermJD(year, (stOrder+1)%24)
//    val t1 = GetDateTimeFromJulianDay(jd1).withZoneSameInstant(ZoneId.of("+08:00")).toLocalDateTime()
//    return arrayOf(t0,t1)
    return t
}

data class solarTermInfo(
        val name: String, //节气的名称
        val t: LocalDateTime //节气的北京时间
)