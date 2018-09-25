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
 * ——————————————————————————————————————————————
 * 这份代码由https://github.com/linuxdeepin/go-lib/blob/master/calendar/lunar/iau1980.go转写面来，
 * 上面的注释是原代码文件中的。
 */
package org.lzh.lunar

data class NutationParameter(val D: Double, val M: Double, val Mp: Double, val F: Double, val Omega: Double)

// T 是  儒略世纪数
// 返回 弧度
fun GetEarthNutationParameter(T: Double): NutationParameter {
    val T2 = T * T
    val T3 = T2 * T

    /*平距角（如月对地心的角距离）*/
    val D = Math.toRadians(297.85036 + 445267.111480 * T - 0.0019142 * T2 + T3 / 189474.0)

    /*太阳（地球）平近点角*/
    val M = Math.toRadians(357.52772 + 35999.050340 * T - 0.0001603 * T2 - T3 / 300000.0)

    /*月亮平近点角*/
    val Mp = Math.toRadians(134.96298 + 477198.867398 * T + 0.0086972 * T2 + T3 / 56250.0)

    /*月亮纬度参数*/
    val F = Math.toRadians(93.27191 + 483202.017538 * T - 0.0036825 * T2 + T3 / 327270.0)

    /*黄道与月亮平轨道升交点黄经*/
    val Omega = Math.toRadians(125.04452 - 1934.136261 * T + 0.0020708 * T2 + T3 / 450000.0)
    return NutationParameter(D, M, Mp, F, Omega)
}

//天体章动系数类型变量
data class NuationCoefficient(val D: Double,
                              val M: Double,
                              val Mp: Double,
                              val F: Double,
                              val Omega: Double,
                              val Sine1: Double,
                              val Sine2: Double,
                              val Cosine1: Double,
                              val Cosine2: Double
)

//由于kotlin不能隐式转换，下面的数据通过电子表格进行了处理，保留小点后一位
val nuation = arrayOf(
        NuationCoefficient(0.0, 0.0, 0.0, 0.0, 1.0, -171996.0, -174.2, 92025.0, 8.9),
        NuationCoefficient(-2.0, 0.0, 0.0, 2.0, 2.0, -13187.0, -1.6, 5736.0, -3.1),
        NuationCoefficient(0.0, 0.0, 0.0, 2.0, 2.0, -2274.0, -0.2, 977.0, -0.5),
        NuationCoefficient(0.0, 0.0, 0.0, 0.0, 2.0, 2062.0, 0.2, -895.0, 0.5),
        NuationCoefficient(0.0, 1.0, 0.0, 0.0, 0.0, 1426.0, -3.4, 54.0, -0.1),
        NuationCoefficient(0.0, 0.0, 1.0, 0.0, 0.0, 712.0, 0.1, -7.0, 0.0),
        NuationCoefficient(-2.0, 1.0, 0.0, 2.0, 2.0, -517.0, 1.2, 224.0, -0.6),
        NuationCoefficient(0.0, 0.0, 0.0, 2.0, 1.0, -386.0, -0.4, 200.0, 0.0),
        NuationCoefficient(0.0, 0.0, 1.0, 2.0, 2.0, -301.0, 0.0, 129.0, -0.1),
        NuationCoefficient(-2.0, -1.0, 0.0, 2.0, 2.0, 217.0, -0.5, -95.0, 0.3),
        NuationCoefficient(-2.0, 0.0, 1.0, 0.0, 0.0, -158.0, 0.0, 0.0, 0.0),
        NuationCoefficient(-2.0, 0.0, 0.0, 2.0, 1.0, 129.0, 0.1, -70.0, 0.0),
        NuationCoefficient(0.0, 0.0, -1.0, 2.0, 2.0, 123.0, 0.0, -53.0, 0.0),
        NuationCoefficient(2.0, 0.0, 0.0, 0.0, 0.0, 63.0, 0.0, 0.0, 0.0),
        NuationCoefficient(0.0, 0.0, 1.0, 0.0, 1.0, 63.0, 0.1, -33.0, 0.0),
        NuationCoefficient(2.0, 0.0, -1.0, 2.0, 2.0, -59.0, 0.0, 26.0, 0.0),
        NuationCoefficient(0.0, 0.0, -1.0, 0.0, 1.0, -58.0, -0.1, 32.0, 0.0),
        NuationCoefficient(0.0, 0.0, 1.0, 2.0, 1.0, -51.0, 0.0, 27.0, 0.0),
        NuationCoefficient(-2.0, 0.0, 2.0, 0.0, 0.0, 48.0, 0.0, 0.0, 0.0),
        NuationCoefficient(0.0, 0.0, -2.0, 2.0, 1.0, 46.0, 0.0, -24.0, 0.0),
        NuationCoefficient(2.0, 0.0, 0.0, 2.0, 2.0, -38.0, 0.0, 16.0, 0.0),
        NuationCoefficient(0.0, 0.0, 2.0, 2.0, 2.0, -31.0, 0.0, 13.0, 0.0),
        NuationCoefficient(0.0, 0.0, 2.0, 0.0, 0.0, 29.0, 0.0, 0.0, 0.0),
        NuationCoefficient(-2.0, 0.0, 1.0, 2.0, 2.0, 29.0, 0.0, -12.0, 0.0),
        NuationCoefficient(0.0, 0.0, 0.0, 2.0, 0.0, 26.0, 0.0, 0.0, 0.0),
        NuationCoefficient(-2.0, 0.0, 0.0, 2.0, 0.0, -22.0, 0.0, 0.0, 0.0),
        NuationCoefficient(0.0, 0.0, -1.0, 2.0, 1.0, 21.0, 0.0, -10.0, 0.0),
        NuationCoefficient(0.0, 2.0, 0.0, 0.0, 0.0, 17.0, -0.1, 0.0, 0.0),
        NuationCoefficient(2.0, 0.0, -1.0, 0.0, 1.0, 16.0, 0.0, -8.0, 0.0),
        NuationCoefficient(-2.0, 2.0, 0.0, 2.0, 2.0, -16.0, 0.1, 7.0, 0.0),
        NuationCoefficient(0.0, 1.0, 0.0, 0.0, 1.0, -15.0, 0.0, 9.0, 0.0),
        NuationCoefficient(-2.0, 0.0, 1.0, 0.0, 1.0, -13.0, 0.0, 7.0, 0.0),
        NuationCoefficient(0.0, -1.0, 0.0, 0.0, 1.0, -12.0, 0.0, 6.0, 0.0),
        NuationCoefficient(0.0, 0.0, 2.0, -2.0, 0.0, 11.0, 0.0, 0.0, 0.0),
        NuationCoefficient(2.0, 0.0, -1.0, 2.0, 1.0, -10.0, 0.0, 5.0, 0.0),
        NuationCoefficient(2.0, 0.0, 1.0, 2.0, 2.0, -8.0, 0.0, 3.0, 0.0),
        NuationCoefficient(0.0, 1.0, 0.0, 2.0, 2.0, 7.0, 0.0, -3.0, 0.0),
        NuationCoefficient(-2.0, 1.0, 1.0, 0.0, 0.0, -7.0, 0.0, 0.0, 0.0),
        NuationCoefficient(0.0, -1.0, 0.0, 2.0, 2.0, -7.0, 0.0, 3.0, 0.0),
        NuationCoefficient(2.0, 0.0, 0.0, 2.0, 1.0, -7.0, 0.0, 3.0, 0.0),
        NuationCoefficient(2.0, 0.0, 1.0, 0.0, 0.0, 6.0, 0.0, 0.0, 0.0),
        NuationCoefficient(-2.0, 0.0, 2.0, 2.0, 2.0, 6.0, 0.0, -3.0, 0.0),
        NuationCoefficient(-2.0, 0.0, 1.0, 2.0, 1.0, 6.0, 0.0, -3.0, 0.0),
        NuationCoefficient(2.0, 0.0, -2.0, 0.0, 1.0, -6.0, 0.0, 3.0, 0.0),
        NuationCoefficient(2.0, 0.0, 0.0, 0.0, 1.0, -6.0, 0.0, 3.0, 0.0),
        NuationCoefficient(0.0, -1.0, 1.0, 0.0, 0.0, 5.0, 0.0, 0.0, 0.0),
        NuationCoefficient(-2.0, -1.0, 0.0, 2.0, 1.0, -5.0, 0.0, 3.0, 0.0),
        NuationCoefficient(-2.0, 0.0, 0.0, 0.0, 1.0, -5.0, 0.0, 3.0, 0.0),
        NuationCoefficient(0.0, 0.0, 2.0, 2.0, 1.0, -5.0, 0.0, 3.0, 0.0),
        NuationCoefficient(-2.0, 0.0, 2.0, 0.0, 1.0, 4.0, 0.0, 0.0, 0.0),
        NuationCoefficient(-2.0, 1.0, 0.0, 2.0, 1.0, 4.0, 0.0, 0.0, 0.0),
        NuationCoefficient(0.0, 0.0, 1.0, -2.0, 0.0, 4.0, 0.0, 0.0, 0.0),
        NuationCoefficient(-1.0, 0.0, 1.0, 0.0, 0.0, -4.0, 0.0, 0.0, 0.0),
        NuationCoefficient(-2.0, 1.0, 0.0, 0.0, 0.0, -4.0, 0.0, 0.0, 0.0),
        NuationCoefficient(1.0, 0.0, 0.0, 0.0, 0.0, -4.0, 0.0, 0.0, 0.0),
        NuationCoefficient(0.0, 0.0, 1.0, 2.0, 0.0, 3.0, 0.0, 0.0, 0.0),
        NuationCoefficient(0.0, 0.0, -2.0, 2.0, 2.0, -3.0, 0.0, 0.0, 0.0),
        NuationCoefficient(-1.0, -1.0, 1.0, 0.0, 0.0, -3.0, 0.0, 0.0, 0.0),
        NuationCoefficient(0.0, 1.0, 1.0, 0.0, 0.0, -3.0, 0.0, 0.0, 0.0),
        NuationCoefficient(0.0, -1.0, 1.0, 2.0, 2.0, -3.0, 0.0, 0.0, 0.0),
        NuationCoefficient(2.0, -1.0, -1.0, 2.0, 2.0, -3.0, 0.0, 0.0, 0.0),
        NuationCoefficient(0.0, 0.0, 3.0, 2.0, 2.0, -3.0, 0.0, 0.0, 0.0),
        NuationCoefficient(2.0, -1.0, 0.0, 2.0, 2.0, -3.0, 0.0, 0.0, 0.0)
)

val coefficient = Math.toRadians(0.0001 / 3600.0)

//计算某时刻的黄经章动干扰量
// T 儒略世纪数
// 返回弧度
fun CalcEarthLongitudeNutation(T: Double): Double {
    val (D, M, Mp, F, Omega) = GetEarthNutationParameter(T)
    var result  = 0.0
    for (n in nuation) {
        val theta = n.D * D + n.M * M + n.Mp * Mp + n.F * F + n.Omega * Omega
        result += (n.Sine1 + n.Sine2 * T) * Math.sin(theta)
    }
    //乘以章动表的系数 0.0001 角秒
    return result * coefficient
}

/*计算某时刻的黄赤交角章动干扰量，dt是儒略千年数，返回值单位是度*/

// 计算某时刻的黄赤交角章动干扰量
// dt 是儒略世纪数
// 返回弧度
fun CalcEarthObliquityNutation(dt: Double): Double {
    val (D, M, Mp, F, Omega) = GetEarthNutationParameter(dt)
    var result = 0.0
//    val len_nuation = len(nuation)
//    for i := 0; i < len_nuation; i++ {
    for (n in nuation) {
        // sita 弧度
//        n := nuation[i]
        val theta = n.D * D + n.M * M + n.Mp * Mp + n.F * F + n.Omega * Omega
        result += (n.Cosine1 + n.Cosine2 * dt) * Math.cos(theta)
    }
    //乘以章动表的系数 0.0001 角秒
    return result * coefficient
}