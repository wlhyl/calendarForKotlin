package pub.teanote.lunarcalendar.lunar

// 月地心黄经系数
data class MoonEclipticLongitudeCoeff(
        val D: Double,
        val M: Double,
        val Mp: Double,
        val F: Double,
        val EiA: Double,
        val ErA: Double
)

/*
    月球黄经周期项(ΣI)及距离(Σr).
    黄经单位:0.000001度,距离单位:0.001千米.
--------------------------------------------------
  角度的组合系数  ΣI的各项振幅A  Σr的各项振幅A
  D  M  M' F        (正弦振幅)       (余弦振幅)
--------------------------------------------------
*/
val MoonLongitude = arrayOf(
        MoonEclipticLongitudeCoeff(0.0, 0.0, 1.0, 0.0, 6288744.0, -20905355.0),
        MoonEclipticLongitudeCoeff(2.0, 0.0, -1.0, 0.0, 1274027.0, -3699111.0),
        MoonEclipticLongitudeCoeff(2.0, 0.0, 0.0, 0.0, 658314.0, -2955968.0),
        MoonEclipticLongitudeCoeff(0.0, 0.0, 2.0, 0.0, 213618.0, -569925.0),
        MoonEclipticLongitudeCoeff(0.0, 1.0, 0.0, 0.0, -185116.0, 48888.0),
        MoonEclipticLongitudeCoeff(0.0, 0.0, 0.0, 2.0, -114332.0, -3149.0),
        MoonEclipticLongitudeCoeff(2.0, 0.0, -2.0, 0.0, 58793.0, 246158.0),
        MoonEclipticLongitudeCoeff(2.0, -1.0, -1.0, 0.0, 57066.0, -152138.0),
        MoonEclipticLongitudeCoeff(2.0, 0.0, 1.0, 0.0, 53322.0, -170733.0),
        MoonEclipticLongitudeCoeff(2.0, -1.0, 0.0, 0.0, 45758.0, -204586.0),
        MoonEclipticLongitudeCoeff(0.0, 1.0, -1.0, 0.0, -40923.0, -129620.0),
        MoonEclipticLongitudeCoeff(1.0, 0.0, 0.0, 0.0, -34720.0, 108743.0),
        MoonEclipticLongitudeCoeff(0.0, 1.0, 1.0, 0.0, -30383.0, 104755.0),
        MoonEclipticLongitudeCoeff(2.0, 0.0, 0.0, -2.0, 15327.0, 10321.0),
        MoonEclipticLongitudeCoeff(0.0, 0.0, 1.0, 2.0, -12528.0, 0.0),
        MoonEclipticLongitudeCoeff(0.0, 0.0, 1.0, -2.0, 10980.0, 79661.0),
        MoonEclipticLongitudeCoeff(4.0, 0.0, -1.0, 0.0, 10675.0, -34782.0),
        MoonEclipticLongitudeCoeff(0.0, 0.0, 3.0, 0.0, 10034.0, -23210.0),
        MoonEclipticLongitudeCoeff(4.0, 0.0, -2.0, 0.0, 8548.0, -21636.0),
        MoonEclipticLongitudeCoeff(2.0, 1.0, -1.0, 0.0, -7888.0, 24208.0),
        MoonEclipticLongitudeCoeff(2.0, 1.0, 0.0, 0.0, -6766.0, 30824.0),
        MoonEclipticLongitudeCoeff(1.0, 0.0, -1.0, 0.0, -5163.0, -8379.0),
        MoonEclipticLongitudeCoeff(1.0, 1.0, 0.0, 0.0, 4987.0, -16675.0),
        MoonEclipticLongitudeCoeff(2.0, -1.0, 1.0, 0.0, 4036.0, -12831.0),
        MoonEclipticLongitudeCoeff(2.0, 0.0, 2.0, 0.0, 3994.0, -10445.0),
        MoonEclipticLongitudeCoeff(4.0, 0.0, 0.0, 0.0, 3861.0, -11650.0),
        MoonEclipticLongitudeCoeff(2.0, 0.0, -3.0, 0.0, 3665.0, 14403.0),
        MoonEclipticLongitudeCoeff(0.0, 1.0, -2.0, 0.0, -2689.0, -7003.0),
        MoonEclipticLongitudeCoeff(2.0, 0.0, -1.0, 2.0, -2602.0, 0.0),
        MoonEclipticLongitudeCoeff(2.0, -1.0, -2.0, 0.0, 2390.0, 10056.0),
        MoonEclipticLongitudeCoeff(1.0, 0.0, 1.0, 0.0, -2348.0, 6322.0),
        MoonEclipticLongitudeCoeff(2.0, -2.0, 0.0, 0.0, 2236.0, -9884.0),
        MoonEclipticLongitudeCoeff(0.0, 1.0, 2.0, 0.0, -2120.0, 5751.0),
        MoonEclipticLongitudeCoeff(0.0, 2.0, 0.0, 0.0, -2069.0, 0.0),
        MoonEclipticLongitudeCoeff(2.0, -2.0, -1.0, 0.0, 2048.0, -4950.0),
        MoonEclipticLongitudeCoeff(2.0, 0.0, 1.0, -2.0, -1773.0, 4130.0),
        MoonEclipticLongitudeCoeff(2.0, 0.0, 0.0, 2.0, -1595.0, 0.0),
        MoonEclipticLongitudeCoeff(4.0, -1.0, -1.0, 0.0, 1215.0, -3958.0),
        MoonEclipticLongitudeCoeff(0.0, 0.0, 2.0, 2.0, -1110.0, 0.0),
        MoonEclipticLongitudeCoeff(3.0, 0.0, -1.0, 0.0, -892.0, 3258.0),
        MoonEclipticLongitudeCoeff(2.0, 1.0, 1.0, 0.0, -810.0, 2616.0),
        MoonEclipticLongitudeCoeff(4.0, -1.0, -2.0, 0.0, 759.0, -1897.0),
        MoonEclipticLongitudeCoeff(0.0, 2.0, -1.0, 0.0, -713.0, -2117.0),
        MoonEclipticLongitudeCoeff(2.0, 2.0, -1.0, 0.0, -700.0, 2354.0),
        MoonEclipticLongitudeCoeff(2.0, 1.0, -2.0, 0.0, 691.0, 0.0),
        MoonEclipticLongitudeCoeff(2.0, -1.0, 0.0, -2.0, 596.0, 0.0),
        MoonEclipticLongitudeCoeff(4.0, 0.0, 1.0, 0.0, 549.0, -1423.0),
        MoonEclipticLongitudeCoeff(0.0, 0.0, 4.0, 0.0, 537.0, -1117.0),
        MoonEclipticLongitudeCoeff(4.0, -1.0, 0.0, 0.0, 520.0, -1571.0),
        MoonEclipticLongitudeCoeff(1.0, 0.0, -2.0, 0.0, -487.0, -1739.0),
        MoonEclipticLongitudeCoeff(2.0, 1.0, 0.0, -2.0, -399.0, 0.0),
        MoonEclipticLongitudeCoeff(0.0, 0.0, 2.0, -2.0, -381.0, -4421.0),
        MoonEclipticLongitudeCoeff(1.0, 1.0, 1.0, 0.0, 351.0, 0.0),
        MoonEclipticLongitudeCoeff(3.0, 0.0, -2.0, 0.0, -340.0, 0.0),
        MoonEclipticLongitudeCoeff(4.0, 0.0, -3.0, 0.0, 330.0, 0.0),
        MoonEclipticLongitudeCoeff(2.0, -1.0, 2.0, 0.0, 327.0, 0.0),
        MoonEclipticLongitudeCoeff(0.0, 2.0, 1.0, 0.0, -323.0, 1165.0),
        MoonEclipticLongitudeCoeff(1.0, 1.0, -1.0, 0.0, 299.0, 0.0),
        MoonEclipticLongitudeCoeff(2.0, 0.0, 3.0, 0.0, 294.0, 0.0),
        MoonEclipticLongitudeCoeff(2.0, 0.0, -1.0, -2.0, 0.0, 8752.0)
)
