package pub.teanote.lunarcalendar.lunar
import pub.teanote.lunarcalendar.util.getJulianCentury
// 参数 T 儒略世纪数
// 返回 弧度
data  class MoonEclipticParameter(val Lp: Double, val D: Double, val M: Double, val Mp: Double, val F: Double, val E: Double)
fun GetMoonEclipticParameter(T: Double ): MoonEclipticParameter {
    val T2 = T * T
    val T3 = T2 * T
    val T4 = T3 * T

    /*月球平黄经*/
    val Lp = Mod2Pi(Math.toRadians(218.3164591 + 481267.88134236*T - 0.0013268*T2 + T3/538841.0 - T4/65194000.0))

    /*月日距角*/
    val D = Mod2Pi(Math.toRadians(297.8502042 + 445267.1115168*T - 0.0016300*T2 + T3/545868.0 - T4/113065000.0))

    /*太阳平近点角*/
    val M = Mod2Pi(Math.toRadians(357.5291092 + 35999.0502909*T - 0.0001536*T2 + T3/24490000.0))

    /*月亮平近点角*/
    val Mp = Mod2Pi(Math.toRadians(134.9634114 + 477198.8676313*T + 0.0089970*T2 + T3/69699.0 - T4/14712000.0))

    /*月球经度参数(到升交点的平角距离)*/
    val F = Mod2Pi(Math.toRadians(93.2720993 + 483202.0175273*T - 0.0034029*T2 - T3/3526000.0 + T4/863310000.0))

    /* 反映地球轨道偏心率变化的辅助参量 */
    val E = 1 - 0.002516*T - 0.0000074*T2
    return MoonEclipticParameter(Lp, D, M, Mp, F, E)
}

/*计算月球地心黄经周期项的和*/
fun CalcMoonECLongitudePeriodic(D: Double, M: Double, Mp: Double, F: Double, E:Double): Double {
    var EI =0.0
    for (l in MoonLongitude) {
        val theta = l.D*D + l.M*M + l.Mp*Mp + l.F*F
        EI += l.EiA * Math.sin(theta) * Math.pow(E, Math.abs(l.M))
    }
    // fmt.Printf("EI = %f\n", EI)
    return EI
}

/*计算金星摄动,木星摄动以及地球扁率摄动对月球地心黄经的影响, T 是儒略世纪数，Lp和F单位是弧度*/
// A1 = 119.75 + 131.849 * T                                             （4.13式）
// A2 = 53.09 + 479264.290 * T                                           （4.14式）
// A3 = 313.45 + 481266.484 * T                                          （4.15式）
fun CalcMoonLongitudePerturbation(T: Double, Lp: Double, F: Double): Double {
    val A1 = Mod2Pi(Math.toRadians(119.75 + 131.849*T))
    val A2 = Mod2Pi(Math.toRadians(53.09 + 479264.290*T))

    return 3958.0*Math.sin(A1) + 1962.0*Math.sin(Lp-F) + 318.0*Math.sin(A2)
}

/*计算月球地心黄经*/
// jd 儒略日
// 返回 弧度
fun GetMoonEclipticLongitudeEC(jd: Double): Double {
    val T = getJulianCentury(jd)
    val(Lp, D, M, Mp, F, E )= GetMoonEclipticParameter(T)
    // Lp 计算是正确的
    // fmt.Printf("Lp = %f\n", Lp)

    /*计算月球地心黄经周期项*/
    var EI = CalcMoonECLongitudePeriodic(D, M, Mp, F, E)

    /*修正金星,木星以及地球扁率摄动*/
    EI += CalcMoonLongitudePerturbation(T, Lp, F)

    var longitude = Lp + Math.toRadians(EI/1000000.0)

    /*计算天体章动干扰*/
    longitude += CalcEarthLongitudeNutation(T)
    return longitude
}