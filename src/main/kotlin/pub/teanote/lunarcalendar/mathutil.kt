package pub.teanote.lunarcalendar

import pub.teanote.sweph.DateTimeData
import pub.teanote.sweph.SE_GREG_CAL
import pub.teanote.sweph.swe_revjul
import pub.teanote.sweph.swe_utc_time_zone
import kotlin.math.abs

/**
 * ModPi 把角度限制在[-180, 180]之间
 */
internal fun mod180(r0: Double): Double {
    var r = r0
    while ( r < -180) {
        r += 360
    }
    while ( r > 180) {
        r -= 360
    }
    return r
}

internal  fun getUT8DateTimeFromJd(jd :Double) :DateTimeData{

    val t = swe_revjul(jd, SE_GREG_CAL)
    val h = t.hour.toInt()
    val mi = ((t.hour - h) * 60).toInt()
    val sec = ((t.hour - h) * 60 - mi) * 60

    // 将新月的jd换算到东八区
    return swe_utc_time_zone(t.year, t.month, t.day, h, mi, sec, -8.0)
}

/**
 * NewtonIteration 牛顿迭代法求解方程的根
 */
internal inline fun newtonIteration(x_0: Double , f: (Double)->Double): Double {
    val epsilon = 1e-7
    val delta = 5e-6
    var x = 0.0
    var x0 = x_0

    while (true) {
        x = x0
        val fx = f(x)
        // 导数
        val fpx = (f(x+delta) - f(x)) / delta
        x0 = x - fx/fpx
        if (abs(x0-x) <= epsilon) {
            break
        }
    }
    return x
}
