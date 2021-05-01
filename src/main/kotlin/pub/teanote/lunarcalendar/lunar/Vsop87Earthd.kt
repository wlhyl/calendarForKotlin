
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
 * You should have received lunar copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * ——————————————————————
 * 以上是原代码的注释
 */

package pub.teanote.lunarcalendar.lunar
import pub.teanote.lunarcalendar.util.getJulianCentury
import pub.teanote.lunarcalendar.util.getJulianThousandYears

/**
 * 按儒略日计算地球的日心黄经
 *
 * 参数： jd 儒略日
 * 返回 地球的日心黄经，单位是弧度(rad)
 */
internal fun getSunEclipticLongitudeForEarth(jd: Double): Double {
    val t = getJulianThousandYears(jd)
    val L0 = getEarthL0(t)
    val L1 = GetEarthL1(t)
    val L2 = GetEarthL2(t)
    val L3 = GetEarthL3(t)
    val L4 = GetEarthL4(t)
    val L5 = GetEarthL5(t)
//    L := ((((L5*t+L4)*t+L3)*t+L2)*t+L1)*t + L0
    val L = L5 + L4 + L3 + L2 + L1 + L0
    return Mod2Pi(L)
}

/**
 * 按儒略日计算地球的日心黄纬
 *
 * 参数 jd  儒略日
 * 返回 地球的日心黄纬，单位是弧度(rad)
 */
internal fun getSunEclipticLatitudeForEarth(jd: Double) : Double {
    val t = getJulianThousandYears(jd)
    val B0 = getEarthB0(t)
    val B1 = GetEarthB1(t)
    val B2 = getEarthB2(t)
    val B3 = getEarthB3(t)
    val B4 = getEarthB4(t)
    val B5 = GetEarthB5(t)
//    B := ((((B4*t)+B3)*t+B2)*t+B1)*t + B0
    val B = B5+B4+B3+B2+B1+ B0
    return B
}

/**
 * 按照儒略日计算地球和太阳的距离
 *
 * 参数 jd  儒略日
 * 返回 地球和太阳的距离，单位是天文单位(au)
 */
internal fun getSunRadiusForEarth(jd: Double): Double {
    val t = getJulianThousandYears(jd)
    val R0 = getEarthR0(t)
    val R1 = getEarthR1(t)
    val R2 = getEarthR2(t)
    val R3 = getEarthR3(t)
    val R4 = getEarthR4(t)
    val R5 = getEarthR5(t)
//    val R = ((((R5*t+R4)*t+R3)*t+R2)*t+R1)*t + R0
    val R = R5+R4+R3+R2+R1+ R0
    return R
}

/**
 * 用于把vsop87理论算出来的经度转换成fk5目视系统的经度的修正值，参考 Jean Meeus 的 Astronomical
 * Algorithms 第二版(1998)第32章219页(32.3)式
 *
 * 参数 l
 *            vsop87经度(rad)
 * 参数 b
 *            vsop87纬度(rad)
 * 参数 jd
 *            儒略日
 * 返回 修正量(rad)
 */
internal fun vsop2Fk5LongitudeCorrection(l: Double, b: Double, jd: Double): Double {
    val t = getJulianCentury(jd)
    val lp = l - Math.toRadians(1.397)*t - Math.toRadians(0.00031)*t*t
    return SecondsToRadians(-0.09033 + 0.03916*(Math.cos(lp)+Math.sin(lp))*Math.tan(b))
}

/**
 * 计算修正后的太阳的地心视黄经
 *
 * 参数 jd
 *            儒略日
 * 返回 修正后的地心黄经(rad)
 */
// 常量
internal val lightAberration = SecondsToRadians(20.4898)

internal fun getEarthEclipticLongitudeForSun(jd: Double): Double {
    // 计算地球的日心黄经
    var l = getSunEclipticLongitudeForEarth(jd)

    // 计算地球的日心黄纬
    val b = getSunEclipticLatitudeForEarth(jd)

    // 修正章动
    l += CalcEarthLongitudeNutation(getJulianCentury(jd))

    // 转换到fk5
    l += vsop2Fk5LongitudeCorrection(l, b, jd)

    // 转换成太阳的地心黄经
    l = Mod2Pi(l + Math.PI)

    // 计算光行差
    // 计算日地距离
    val r = getSunRadiusForEarth(jd)
    // 太阳到地球的光行差参数
    l -= lightAberration / r
    return l
}
