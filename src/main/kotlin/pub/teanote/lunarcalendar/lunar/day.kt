package pub.teanote.lunarcalendar.lunar

import pub.teanote.ganzhiwuxing.DiZhi
import pub.teanote.ganzhiwuxing.GanZhi
import pub.teanote.ganzhiwuxing.TianGan

// Day 保存农历日信息
class Day(
    val Year: Int,   // 农历年
    val Day: Int, // 农历日
    val Month: Month, // 农历月
    private val MonthZhi: Int,    // 农历日所在的月的地支, 1，为子月
    private val SolarTerm: Int    // 0~23 二十四节气 ，-1 非节气
) {
    // 十二月名
    private val monthNames = arrayOf("正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬", "腊")
    // 农历日名
// 月份分为大月和小月，大月三十天，小月二十九天
    private val dayNames = arrayOf(
            "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
            "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"
    )
    //农历节日
    private val lunarFestival = mapOf(
            101 to "春节",
            115 to "元宵节",
            202 to "龙抬头节",
            323 to "妈祖生辰",
            505 to "端午节",
            707 to "七夕情人节",
            715 to "中元节",
            815 to "中秋节",
            909 to "重阳节",
            1015 to "下元节",
            1208 to "腊八节",
            1223 to "小年"
    )

    val MonthName = GetMonthName()
    val DayName = dayNames[Day - 1] // DayName 获取当天的农历日名
    val Festival = GetFestival()
    // SolarTermName 获取当天的二十四节气名
// 没有则返回空字符串
    val SolarTermName = getSolarTermName(SolarTerm)
    // GanZhiMonth 获取当天的月干支
//    val GanZhiMonth =GanZhi(TianGan("甲"), DiZhi("子")) + ((Year-1899)*12 + (MonthZhi - 1) + 12)
    val GanZhiMonth: GanZhi
        get() {
            val y = Year
            var m = (MonthZhi - 2 + 12) % 12 //子月为1,以寅月为1月，卯月为2 月
            if (m == 0) {
                m = 12
            }
            val 太乙积年 = 1936557
            var __积年 = 太乙积年 + y - 1
            if (y < 0) __积年 = __积年 + 1
            var __积算 = __积年 * 12 + 2 + m
            val __月干支 = GanZhi(TianGan("甲"), DiZhi("子")) + (__积算 % 60 - 1)
            return __月干支
        }


    // MonthName 获取当天的农历月名称
    private fun GetMonthName(): String {
        val monthName = monthNames[Month.number - 1]
        if (Month.isLeap) {
            return "闰" + monthName + "月"
        }
        return monthName + "月"
    }

    // Festival 获取当天的农历节日名
// 没有则返回空字符串
    private fun GetFestival(): String {
        val key = Month.number * 100 + Day

//            return lunarFestival[key]?:""
        val lunarFestivalValue = lunarFestival[key]?:""
        if (lunarFestivalValue != "") return lunarFestivalValue

        // 农历腊月（十二月）的最后个一天
        if (Month.number == 12 && Day == Month.days) {
            return "除夕"
        }
        return ""
    }

//    fun getGanZhiMonth(): GanZhi{
//        val y = Year
//        var m = (MonthZhi-2+12) %12) //子月为1,以寅月为1月，卯月为2 月
//        if (m==0){
//            m=12
//        }
//        val 太乙积年 = 1936557
//        var __积年 = 太乙积年 + y - 1
//        if y < 0:
//        __积年 = __积年 + 1
//        var __积算 = __积年 * 12 + 2 + m
//        val __月干支 = GanZhi(TianGan("甲"), DiZhi("子")) + (__积算 % 60 - 1)
//        return __月干支
//    }
}