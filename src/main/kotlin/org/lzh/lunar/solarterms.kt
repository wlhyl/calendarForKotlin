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

package org.lzh.lunar

import org.lzh.util.ToJulianDateHMS

val SolarTermNames = arrayOf(
        "春分",
        "清明",
        "谷雨",
        "立夏",
        "小满",
        "芒种",
        "夏至",
        "小暑",
        "大暑",
        "立秋",
        "处暑",
        "白露",
        "秋分",
        "寒露",
        "霜降",
        "立冬",
        "小雪",
        "大雪",
        "冬至",
        "小寒",
        "大寒",
        "立春",
        "雨水",
        "惊蛰"
)

const val ChunFen = 0
const val QingMing = 1
const val GuYu = 2
const val LiXia = 3
const val XiaoMan = 4
const val MangZhong = 5
const val XiaZhi = 6
const val XiaoShu = 7
const val DaShu = 8
const val LiQiu = 9
const val ChuShu = 10
const val BaiLu = 11
const val QiuFen = 12
const val HanLu = 13
const val ShuangJiang = 14
const val LiDong = 15
const val XiaoXue = 16
const val DaXue = 17
const val DongZhi = 18
const val XiaoHan = 19
const val DaHan = 20
const val LiChun = 21
const val YuShui = 22
const val JingZhe = 23


// GetSolarTermName 获取二十四节气名
fun GetSolarTermName(order: Int): String {
    if (0 <= order && order <= 23) {
        return SolarTermNames[order]
    }
    return ""
}

// GetSolarTermJD 使用牛顿迭代法计算24节气的时间
// f(x) = Vsop87dEarthUtil.getEarthEclipticLongitudeForSun(x) - angle = 0
// year 年
// order 节气序号
// 返回 节气的儒略日力学时间 TD
fun GetSolarTermJD(year: Int, order: Int) :Double {
    val RADIANS_PER_TERM = Math.PI / 12.0
    val angle = order * RADIANS_PER_TERM
    val month = ((order+1)/2+2)%12 + 1
    // 春分 order 0
    // 3 月 20 号
    var day = 6
    if (order%2 == 0) {
        day = 20
    }

    val jd0 = ToJulianDateHMS(year, month, day, 12, 0, 0.0)
    val jd = NewtonIteration(jd0){
        ModPi(GetEarthEclipticLongitudeForSun(it) - angle)
    }

    return jd
}
