import pub.teanote.lunarcalendar.calendar.solarToLunar
import pub.teanote.lunarcalendar.lunar.getSolarTermJD

fun main(args: Array<String>) {
   val a=  getSolarTermJD(2022, 0)
    println(a)
    val t = solarToLunar(2021,5,1)

    println(t)
//    val defaultZone = ZoneId.systemDefault()
////    val localT = ZonedDateTime.of(2018,3,18,0,0,0,0,defaultZone)
//    val localT = ZonedDateTime.now(defaultZone)
//    val utcT = localT.withZoneSameInstant(ZoneId.of("UTC"))
//    val JD = JulianDate.of(utcT.toLocalDateTime()).doubleValue()
//    val JDE0 = JD + 32.184 / 86400.0
//    val JDE1 = JDE0 + 10
//    val t0 = (JDE0 - 2451545.0) / 365250//没有加润秒
//    val t1 = (JDE1 - 2451545.0) / 365250
//    val JDx = CalculateSolarTerms(t0, t1, 180.0)
////    val t = (JD - 2451545.0) / 365250
////val L = Getlongitude(t)
//    (JDx * 365250 + 2451545) - 32.184 / 86400.0
//    val t = JulianDate((JDx * 365250 + 2451545.0) - 32.184 / 86400.0).toLocalDateTime()
//    println(t.atZone(ZoneId.of("UTC")).withZoneSameInstant(defaultZone))
//    val j = GetSolarTermJD(2018, QiuFen)
//    println(j)
//    println(GetDateTimeFromJulianDay(j).withZoneSameInstant(ZoneId.of("Asia/Shanghai")))
//val a = Calendar(2018)
//////val b = GetDayGanZhi(2018,9,22)
//////    println(b)
//    val d = a.SolarDayToLunarDay(10,3)
//    println(d.MonthName)
//    println(d.DayName)
//    println(d.GanZhiMonth)
//    print(GetDayGanZhi(2018,10,3))
//    println(d)
//val t= ZonedDateTime.of(2018,1,3,12,0,0,0,defaultZone)
////println(t.toLocalDate().atTime(0,0,0).atZone(defaultZone).withZoneSameInstant(ZoneId.of("UTC")))
//    val t1= ZonedDateTime.of(2018,1,4,0,0,0,0,defaultZone)
//    println(deltaDays(t.toLocalDate(),t1.toLocalDate()))
//    println(JulianDate.of(t1.toLocalDate()).doubleValue())
//    println(JulianDate.of(t1.toLocalDate()).integer)
//    println(t.dayOfYear)
//    println(t.withZoneSameInstant(ZoneId.of("UTC")).dayOfYear)
//    val date1 = JulianDate.of(t.withZoneSameInstant(ZoneId.of("UTC")).toLocalDate())
//    println(date1.doubleValue())
//    println(date1.integer)
}

//fun Getlongitude(t: Double): Double {
//    val L0 = Vsop87DEarth.Earth_L0(t)
//    val L1 = Vsop87DEarth.Earth_L1(t)
//    val L2 = Vsop87DEarth.Earth_L2(t)
//    val L3 = Vsop87DEarth.Earth_L3(t)
//    val L4 = Vsop87DEarth.Earth_L4(t)
//    val L5 = Vsop87DEarth.Earth_L5(t)
//    var L = (Math.toDegrees(L0 + L1 + L2 + L3 + L4 + L5) + 180) % 360
//    if (L < 0) L += 360
//    val λ = L + Math.toDegrees(CalcEarthLongitudeNutation(t*10))
////    val Ω = 125.04 - 1934.136*t*10.0 //t为儒略千年数，乘以10为儒略世纪数
////    val λ = L - 0.00569 -0.00478*Math.sin(Ω)
//    return λ
//
//}
//
//fun CalculateSolarTerms(t0: Double, t1: Double, angle: Double): Double {
//
//    var x0 = t0
//    var x1 = t1
//    var x = (x0 + x1) / 2.0
//    var y1 = Getlongitude(x)
//    while (Math.abs(y1 - angle) > 0.0001) {
////        val y0 = Getlongitude(x0)
//        x = (x0 + x1) / 2.0
//        y1 = Getlongitude(x)
//        if (angle ==0.0 && y1 >270) y1-=360
////       println("${y0} ${y1}")
////       println(Math.abs(y1-180))
////        if (Math.abs(y1-180)<=0.0001)break
//        if (y1 > angle) x1 = x else x0 = x
//    }
//    return x
//}