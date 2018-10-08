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

package org.lzh.util
import jodd.time.JulianDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

//// GetDateFromJulianDay 从儒略日中获取公历的日期
//fun GetDateFromJulianDay(jd: Double): Triple<Int, Int, Int> {
//    /*
//     * This algorithm is taken from
//     * "Numerical Recipes in c, 2nd Ed." (1992), pp. 14-15
//     * and converted to integer math.
//     * The electronic version of the book is freely available
//     * at http://www.nr.com/ , under "Obsolete Versions - Older
//     * book and code versions.
//     */
////    val JD_GREG_CAL = 2299161
//////    val JB_MAX_WITHOUT_OVERFLOW = 107374182
////    val julian = Math.floor(jd + 0.5)
//////
//////    var ta, jalpha, tb, tc, td, te int64
//////
////    if (julian >= JD_GREG_CAL) {
////        jalpha = (4*(julian-1867216) - 1) / 146097
////        ta = int64(julian) + 1 + jalpha - jalpha/4
////    } else if julian < 0 {
////        ta = julian + 36525*(1-julian/36525)
////    } else {
////        ta = julian
////    }
////    tb = ta + 1524
////    if tb <= JB_MAX_WITHOUT_OVERFLOW {
////        tc = (tb*20 - 2442) / 7305
////    } else {
////        tc = int64((uint64(tb)*20 - 2442) / 7305)
////    }
////    td = 365*tc + tc/4
////    te = ((tb - td) * 10000) / 306001
////
////    dd = int(tb - td - (306001*te)/10000)
////
////    mm = int(te - 1)
////    if mm > 12 {
////        mm -= 12
////    }
////    yy = int(tc - 4715)
////    if mm > 2 {
////        yy--
////    }
////    if julian < 0 {
////        yy -= int(100 * (1 - julian/36525))
////    }
////
////    return
////    算法取自许剑伟译的《天文算法》
//    val JD = jd
//    val Z = Math.floor(JD + 0.5).toInt()
//    val F = JD + 0.5 - Z
//    val A = if (Z < 2299161) {
//        Z
//    } else {
//        val a = Math.floor((Z - 1867216.25) / 36524.25).toInt()
//        Z + 1 + a - a / 4
//    }
//    val B = A + 1524
//    val C = ((B - 122.1) / 365.25).toInt()
//    val D = (365.25 * C).toInt()
//    val E = Math.floor((B - D) / 30.6001).toInt()
//
//    val d = B - D - Math.floor(30.6001 * E).toInt() + F
//    val m = if (E < 14) E - 1 else E - 13
//    val y = if (m > 2) C - 4716 else C - 4715
//    return Triple(y, m, d.toInt())
//}
//
//// GetTimeFromJulianDay 从儒略日中获取时间 时分秒
//fun GetTimeFromJulianDay(jd: Double):Triple<Int, Int, Int>{
//    val frac = jd - Math.floor(jd)
//    val s = Math.floor(frac * 24.0 * 60.0 * 60.0).toInt()
//
//    val hour = ((s / (60 * 60)) + 12) % 24
//    val minute = (s / (60)) % 60
//    val second = s % 60
//    return Triple(hour,minute,second)
//}

// GetDateTimeFromJulianDay 将儒略日转换为 time.Time
// 其中包含了 TT 到 UTC 的转换
fun GetDateTimeFromJulianDay(JD: Double): ZonedDateTime {
    var jd = JD
    var t = JulianDate(jd).toLocalDateTime()
    val yy = t.year
    val mm = t.month.value
    //  TT -> UTC
    jd -= GetDeltaT(yy, mm) / 86400
    t = JulianDate(jd).toLocalDateTime()
    return t.atZone(ZoneId.of("UTC"))
}

//GetJulianDayFromDateTime 将 datetime.datetime 转换为儒略日
//其中包含了 UTC 到 TT 的转换
//传入参数为UTC时间
//返回力学时的儒略日，浮点数
fun GetJulianDayFromDateTime(t: LocalDateTime):Double {
    var jd = JulianDate.of(t).doubleValue()
    val yy = t.year
    val mm = t.monthValue
//    UTC -> TT
    jd += GetDeltaT(yy, mm) / 86400
    return jd
}