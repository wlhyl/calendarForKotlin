package pub.teanote.lunarcalendar.lunar

import pub.teanote.lunarcalendar.util.toJulianDateHMS

internal val SolarTermNames = listOf(
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

internal const val ChunFen = 0
internal const val QingMing = 1
internal const val GuYu = 2
internal const val LiXia = 3
internal const val XiaoMan = 4
internal const val MangZhong = 5
internal const val XiaZhi = 6
internal const val XiaoShu = 7
internal const val DaShu = 8
internal const val LiQiu = 9
internal const val ChuShu = 10
internal const val BaiLu = 11
internal const val QiuFen = 12
internal const val HanLu = 13
internal const val ShuangJiang = 14
internal const val LiDong = 15
internal const val XiaoXue = 16
internal const val DaXue = 17
internal const val DongZhi = 18
internal const val XiaoHan = 19
internal const val DaHan = 20
internal const val LiChun = 21
internal const val YuShui = 22
internal const val JingZhe = 23


// GetSolarTermName 获取二十四节气名
internal fun getSolarTermName(order: Int): String {
    if (0 <= order && order <= 23) {
        return SolarTermNames[order]
    }
    return ""
}

/**
 * getSolarTermJD 使用牛顿迭代法计算24节气的时间
 * f(x) = Vsop87dEarthUtil.getEarthEclipticLongitudeForSun(x) - angle = 0
 * year 年
 * order 节气序号
 * 返回 节气的儒略日力学时间 TD
 */
internal fun getSolarTermJD(year: Int, order: Int) :Double {
    val RADIANS_PER_TERM = Math.PI / 12.0
    val angle = order * RADIANS_PER_TERM
    val month = ((order+1)/2+2)%12 + 1

    // 春分 order 0
    // 3 月 20 号
    var day = 6
    if (order%2 == 0) {
        day = 20
    }

    val jd0 = toJulianDateHMS(year, month, day, 12, 0, 0.0)
    val jd = NewtonIteration(jd0){
        ModPi(getEarthEclipticLongitudeForSun(it) - angle)
    }

    return jd
}
