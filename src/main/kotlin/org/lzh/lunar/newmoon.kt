package org.lzh.lunar


fun getNewMoonJD(jd0: Double):Double {
    val jd = NewtonIteration(
    jd0){
        ModPi(GetEarthEclipticLongitudeForSun(it) - GetMoonEclipticLongitudeEC(it))
    }
    return jd
}
