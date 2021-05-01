/*
 * Copyright (C) 2014 ~ 2018 Deepin Technology Co., Ltd.
 *
 * Author:     jouyouyun <jouyouwen717@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pub.teanote.lunarcalendar.util

import kotlin.math.floor

//// IsLeapYear 公历闰年判断
//func IsLeapYear(year int) bool {
//    return (year&3) == 0 && year%100 != 0 || year%400 == 0
//}
//
//var monthDays = []int{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}
//
//// GetSolarMonthDays 获取公历月份的天数
//func GetSolarMonthDays(year, month int) int {
//    if month == 2 && IsLeapYear(year) {
//        return 29
//    } else {
//        return monthDays[month-1]
//    }
//}
//
//// GetYearDaysCount 获取某年的天数
//func GetYearDaysCount(year int) int {
//    if IsLeapYear(year) {
//        return 366
//    }
//    return 365
//}

/**
 * ToJulianDate 计算Gregorian日期的儒略日数，以TT当天中午12点为准(结果是整数)。
 * 算法摘自 http://en.wikipedia.org/wiki/Julian_day
 */
internal fun toJulianDate(year: Int, month: Int, day: Int): Double {
//    var a int = (14 - month) / 12
//    var y int = year + 4800 - a
//    var m int = month + 12*a - 3
//    return day + (153*m+2)/5 + 365*y + y/4 - y/100 + y/400 - 32045
    var y = year
    var m = month
    val d = day

    if (m < 3) {
        y -= 1
        m += 12
    }
    val a = floor(y / 100.0).toInt()
    var b = 0
    if (y > 1582 || (y == 1582 && (m > 10 || (m == 10 || d >= 15)))) {
        b = 2 - a + floor(a / 4.0).toInt()
    }
    return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + d + b - 1524.5
}

/**
 *  ToJulianDateHMS 计算Gregorian时间的儒略日数
 *  算法摘自 http://en.wikipedia.org/wiki/Julian_day
 *  这里没有使用深度原来的代码
 */
internal fun toJulianDateHMS(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Double): Double {
    val jdn = toJulianDate(year, month, day)
    return jdn + (hour-12)/24.0 + minute/1440.0 + second/86400.0
}

/**
 * Gregorian历TT2000年1月1日中午12点的儒略日
 */
internal const val J2000 = 2451545.0

/**
 *  getJulianThousandYears 计算儒略千年数
 */
internal fun getJulianThousandYears(jd: Double):Double {
    //1000年的日数
    val daysOf1000Years = 365250.0
    return (jd - J2000) / daysOf1000Years
}

/**
 *  getJulianCentury 计算儒略世纪数
 */
internal fun getJulianCentury(jd: Double): Double {
    // 100年的日数
    val  daysOfCentury = 36525.0
    return (jd - J2000) / daysOfCentury
}

//// GetWeekday 计算Gregorian日历的星期几
//// 算法摘自 http://en.wikipedia.org/wiki/Zeller%27s_congruence
//// 返回星期几的数字表示，1-6表示星期一到星期六，0表示星期日
//func GetWeekday(y, m, d int) int {
//    if m <= 2 {
//        y -= 1
//        m += 12
//    }
//    c := int(y / 100)
//    y = y % 100
//    w := (d + 13*(m+1)/5 + y + (y / 4) + (c / 4) - 2*c - 1) % 7
//    if w < 0 {
//        w += 7
//    }
//    return w
//}

/**
 *  getDeltaT 计算地球时和UTC的时差，算法摘自
 *  http://eclipse.gsfc.nasa.gov/SEhelp/deltatpoly2004.html NASA网站
 *  ∆T = TT - UT 此算法在-1999年到3000年有效
 */
internal fun getDeltaT(year: Int, month: Int): Double {
    val y = year + (month - 0.5) / 12.0
    when {
        y < -500 -> {
            val u = (year - 1820.0) / 100
            return -20 + 32 * u * u
        }
        y < 500 -> {
            val u = y / 100
            val u2 = u * u
            val u3 = u2 * u
            val u4 = u3 * u
            val u5 = u4 * u
            val u6 = u5 * u
            return 10583.6 - 1014.41 * u + 33.78311 * u2 - 5.952053 * u3 - 0.1798452 * u4 + 0.022174192 * u5 + 0.0090316521 * u6
        }
        year < 1600 -> {
            val u = (y - 1000) / 100
            val u2 = u * u
            val u3 = u2 * u
            val u4 = u3 * u
            val u5 = u4 * u
            val u6 = u5 * u
            return 1574.2 - 556.01 * u + 71.23472 * u2 + 0.319781 * u3 - 0.8503463 * u4 - 0.005050998 * u5 + 0.0083572073 * u6
        }
        year < 1700 -> {
            val t = y - 1600
            val t2 = t * t
            val t3 = t2 * t
            return 120 - 0.9808 * t - 0.01532 * t2 + t3 / 7129
        }
        year < 1800 -> {
            val t = y - 1700
            val t2 = t * t
            val t3 = t2 * t
            val t4 = t3 * t
            return 8.83 + 0.1603 * t - 0.0059285 * t2 + 0.00013336 * t3 - t4 / 1174000
        }
        year < 1860 -> {
            val t = y - 1800
            val t2 = t * t
            val t3 = t2 * t
            val t4 = t3 * t
            val t5 = t4 * t
            val t6 = t5 * t
            val t7 = t6 * t
            return 13.72 - 0.332447 * t + 0.0068612 * t2 + 0.0041116 * t3 - 0.00037436 * t4 + 0.0000121272 * t5 - 0.0000001699 * t6 + 0.000000000875 * t7
        }
        year < 1900 -> {
            val t = y - 1860
            val t2 = t * t
            val t3 = t2 * t
            val t4 = t3 * t
            val t5 = t4 * t
            return 7.62 + 0.5737 * t - 0.251754 * t2 + 0.01680668 * t3 - 0.0004473624 * t4 + t5 / 233174
        }
        year < 1920 -> {
            val t = y - 1900
            val t2 = t * t
            val t3 = t2 * t
            val t4 = t3 * t
            return -2.79 + 1.494119 * t - 0.0598939 * t2 + 0.0061966 * t3 - 0.000197 * t4
        }
        year < 1941 -> {
            val t = y - 1920
            val t2 = t * t
            val t3 = t2 * t
            return 21.20 + 0.84493 * t - 0.076100 * t2 + 0.0020936 * t3
        }
        year < 1961 -> {
            val t = y - 1950
            val t2 = t * t
            val t3 = t2 * t
            return 29.07 + 0.407 * t - t2 / 233 + t3 / 2547
        }
        year < 1986 -> {
            val t = y - 1975
            val t2 = t * t
            val t3 = t2 * t
            return 45.45 + 1.067 * t - t2 / 260 - t3 / 718
        }
        year < 2005 -> {
            val t = y - 2000
            val t2 = t * t
            val t3 = t2 * t
            val t4 = t3 * t
            val t5 = t4 * t
            return 63.86 + 0.3345 * t - 0.060374 * t2 + 0.0017275 * t3 + 0.000651814 * t4 + 0.00002373599 * t5
        }
        year < 2050 -> {
            val t = y - 2000
            val t2 = t * t
            return 62.92 + 0.32217 * t + 0.005589 * t2
        }
        year < 2150 -> {
            val u = (y - 1820) / 100
            val u2 = u * u
            return -20 + 32 * u2 - 0.5628 * (2150 - y)
        }
        else -> {
            val u = (y - 1820) / 100
            val u2 = u * u
            return -20 + 32 * u2
        }
    }
}

/**
 * JDUTC2BeijingTime 儒略日 UTC 时间转换到北京时间
 */
internal fun jDUTC2BeijingTime(utcJD: Double):Double {
    return utcJD + 8.0/24.0
}

// JDBeijingTime2UTC 儒略日 北京时间到 UTC 时间
fun JDBeijingTime2UTC(bjtJD: Double ): Double {
    return bjtJD - 8.0/24.0
}
