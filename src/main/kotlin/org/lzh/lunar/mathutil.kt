package org.lzh.lunar

//// ToRadians 角度转换为弧度
//func ToRadians(degrees float64) float64 {
//    return degrees * math.Pi / 180
//}

//// ToDegrees 弧度转换为角度
//func ToDegrees(rad float64) float64 {
//    return rad * 180 / math.Pi
//}

// SecondsToRadians 把角秒换算成弧度
fun SecondsToRadians(seconds: Double): Double {
    return Math.toRadians(SecondsToDegrees(seconds))
}

// Mod2Pi 把角度限制在[0, 2π]之间
fun Mod2Pi(r0: Double): Double {
    var r=r0
    while ( r < 0) {
        r += Math.PI * 2
    }
    while ( r > 2*Math.PI) {
        r -= Math.PI * 2
    }
    return r
}

// ModPi 把角度限制在[-π, π]之间
fun ModPi(r0: Double): Double {
    var r = r0
    while ( r < -Math.PI) {
        r += Math.PI * 2
    }
    while ( r > Math.PI) {
        r -= Math.PI * 2
    }
    return r
}

// SecondsToDegrees 把角秒换算成角度
fun SecondsToDegrees(seconds: Double): Double {
    return seconds / 3600.0
}

//// DmsToDegrees 把度分秒表示的角度换算成度
//func DmsToDegrees(degrees int, mintues int, seconds float64) float64 {
//    return float64(degrees) + float64(mintues)/60 + seconds/3600
//}
//
//// DmsToSeconds 把度分秒表示的角度换算成角秒(arcsecond)
//func DmsToSeconds(d int, m int, s float64) float64 {
//    return float64(d)*3600 + float64(m)*60 + s
//}
//
//// DmsToRadians 把度分秒表示的角度换算成弧度(rad)
//func DmsToRadians(d int, m int, s float64) float64 {
//    return ToRadians(DmsToDegrees(d, m, s))
//}
//
// NewtonIteration 牛顿迭代法求解方程的根
fun NewtonIteration(x_0: Double , f: (Double)->Double): Double {
    val Epsilon = 1e-7
    val Delta = 5e-6
    var x = 0.0
    var x0 = x_0

    while (true) {
        x = x0
        val fx = f(x)
        // 导数
        val fpx = (f(x+Delta) - f(x-Delta)) / Delta / 2
        x0 = x - fx/fpx
        if (Math.abs(x0-x) <= Epsilon) {
            break
        }
    }
    return x
}
