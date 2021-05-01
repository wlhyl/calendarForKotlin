package pub.teanote.lunarcalendar.lunar


fun getNewMoonJD(jd0: Double):Double {
    val jd = NewtonIteration(
    jd0){
        ModPi(getEarthEclipticLongitudeForSun(it) - GetMoonEclipticLongitudeEC(it))
    }
    return jd
}
