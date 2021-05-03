package pub.teanote.lunarcalendar

import pub.teanote.ganzhiwuxing.DiZhi
import pub.teanote.ganzhiwuxing.GanZhi
import pub.teanote.ganzhiwuxing.TianGan
import pub.teanote.lunarcalendar.exception.CallException
import pub.teanote.sweph.*
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * 传公历年，月，日，返回农历年
 */
class LunarCalendar(
    private  val year :Int, private val month :Int, private val day :Int,
    private val hour :Int = 0, private val minute :Int = 0, private val second :Double = 0.0,
    private val ephePath :String? = null){
    // 从前一年冬至到此年冬至间的25个节气，第25个节气=此年冬至
    private val solarTermJds :List<Double>
    // 从前一年冬至所在农历开始的15个新月的jd
    private val newMoonJDs :List<Double>
    // 从前一年冬至所在农历月开始的15个农历月的初一的儒略日，以东八区时间为准
    private val lunarMonths :List<LunarMonthData>

    // 闰月在lunarMonthJds中的index
    val isLeanYear :Boolean

    //农历年
    val lunarYear :GanZhi
    // 农历月
    val lunarMonth :String
    // 农历日
    val lunarDay :String

    //农历月干支
    val lunarMonthGanZhi :GanZhi

    // 日干支
    val lunarDayGanZhi : GanZhi

    // 时干支
    val timeGanZhi : GanZhi

    //节
    val solarTermFirst :SolarTermData

    //气
    val solarTermSecond :SolarTermData

    init {
        this.solarTermJds = this.get25SolarTermJds(this.year - 1)
        this.newMoonJDs = this.get15NewMoonJDs(this.solarTermJds[0])
        this.lunarMonths = this.get15LunarMonthJds(this.newMoonJDs)

        // 计算闰月，如果有闰月，修正月的num
        this.calcLeapMonth(this.lunarMonths, this.solarTermJds.filterIndexed { index, d -> index % 2 == 0 })

        this.isLeanYear = this.lunarMonths.any { it.isLeap }

        // 得到月名
        this.lunarMonths.forEach { lunarMonth ->
            if(lunarMonth.isLeap) lunarMonth.monthName = "闰${monthNames[lunarMonth.num - 1 ]}月"
            else lunarMonth.monthName = "${monthNames[lunarMonth.num - 1 ]}月"
        }

        /*
       将公历转换为农历
       为方便计算，可以取20:00:00，utc此时为12:00:00
       此处默认计算00:00:00
        */
        val currentT = swe_utc_time_zone(this.year, this.month, this.day, this.hour, this.minute, this.second, 8.0)
        val currentJd = swe_julday(
            currentT.year,
            currentT.month,
            currentT.day,
            currentT.hour + currentT.min / 60.0 + currentT.sec / 3600.0,
            SE_GREG_CAL
        )

        // 找出当前日期所在农历月
        var n = 0
        for (i in 0 until this.lunarMonths.size) {
            if (this.lunarMonths[i].jd <= currentJd && currentJd < this.lunarMonths[i + 1].jd) {
                n = i
                break
            }
        }
        this.lunarMonth = this.lunarMonths[n].monthName
        val day = (currentJd - this.lunarMonths[n].jd).toInt()
        this.lunarDay = dayNames[day]


        // 计算年，根据2017年国标，农历年用干支表示
        val firstLunarMonth = this.lunarMonths.first() { it.num == 1 && !it.isLeap}
        this.lunarYear = if(currentJd < firstLunarMonth.jd){
            GanZhi(TianGan("甲"), DiZhi("子")) + (this.year -1 - 1864)
        }else {
            GanZhi(TianGan("甲"), DiZhi("子")) + (this.year - 1864)
        }

        // 计算日柱, 以2017年4月7日，甲子日为起点
        this.lunarDayGanZhi = GanZhi(TianGan("甲"), DiZhi("子")) +
                floor((currentJd - swe_julday(2017, 4, 6, 16.0, SE_GREG_CAL))).toInt()

        // 计算月柱，默认为00:00:00所在的月柱，过节换月柱
        // 大雪的黄经=255度
        val currentSunPosi = swe_calc_ut(currentJd, SE_SUN, SEFLG_SWIEPH)
        if(currentSunPosi.serr.isNotEmpty() || currentSunPosi.rc < -0)
            throw CallException("swe_calc_ut()错误。${currentSunPosi.serr}")
        val monthNum = (swe_degnorm(currentSunPosi.xx[0] - 255) / 30).toInt()
        val monthDiZhi = DiZhi("子") + monthNum

        /*
        求月柱，按节气换年，不能使用农历正月初一换年，如果 2017年1月7日，节气年、农历年都是丙申，
        不能以monthNum < 2，将农历的丙申 - 1
        *、
         */
        val yearGan = if(monthNum < 2){
            GanZhi(TianGan("甲"), DiZhi("子")) + (this.year - 1 - 1864)
        }else {
            GanZhi(TianGan("甲"), DiZhi("子")) + (this.year - 1864)

        }.gan

        this.lunarMonthGanZhi = when(yearGan){
            TianGan("甲"), TianGan("己")-> GanZhi(TianGan("丙") + (monthDiZhi - DiZhi("寅")), monthDiZhi)
            TianGan("乙"), TianGan("庚")-> GanZhi(TianGan("戊"), DiZhi("寅")) + (monthDiZhi - DiZhi("寅"))
            TianGan("丙"), TianGan("辛")-> GanZhi(TianGan("庚") + (monthDiZhi - DiZhi("寅")), monthDiZhi)
            TianGan("丁"), TianGan("壬")-> GanZhi(TianGan("壬") + (monthDiZhi - DiZhi("寅")), monthDiZhi)
            else -> GanZhi(TianGan("甲") + (monthDiZhi - DiZhi("寅")), monthDiZhi)
        }

        // 计算时柱, (hour + 1) / 2 = 时辰数-1, 0点子时=1,丑时=2,辰时=3... 亥时=11,23点=12
        this.timeGanZhi = when(this.lunarDayGanZhi.gan){
            TianGan("甲"), TianGan("己")-> GanZhi(TianGan("甲"),DiZhi("子")) + (this.hour + 1)/2
            TianGan("乙"), TianGan("庚")-> GanZhi(TianGan("丙"),DiZhi("子")) + (this.hour + 1)/2
            TianGan("丙"), TianGan("辛")-> GanZhi(TianGan("戊"),DiZhi("子")) + (this.hour + 1)/2
            TianGan("丁"), TianGan("壬")-> GanZhi(TianGan("庚"),DiZhi("子")) + (this.hour + 1)/2
            else -> GanZhi(TianGan("壬"),DiZhi("子")) + (this.hour + 1)/2
        }

        /*
        计算此日期所在的节气
         */

        val solarTermJd0 = newtonIteration(currentJd){jd ->
            swe_set_ephe_path(this.ephePath)
            val sunPosi = swe_calc_ut(jd, SE_SUN, SEFLG_SWIEPH)
            swe_close()
            if(sunPosi.serr.isNotEmpty() || sunPosi.rc < -0) throw CallException("swe_calc_ut()错误。${sunPosi.serr}")
            mod180(sunPosi.xx[0] - swe_degnorm(monthNum * 30 + 255.0))
        }

        val solarTerm0Time = getUT8DateTimeFromJd(solarTermJd0)
        this.solarTermFirst =  SolarTermData(
            SolarTermNames[monthNum * 2],
            "${solarTerm0Time.year}-${solarTerm0Time.month}-${solarTerm0Time.day} ${solarTerm0Time.hour}:${solarTerm0Time.min}:${solarTerm0Time.sec.roundToInt()}")

        val solarTermJd1 = newtonIteration(solarTermJd0 + 15){jd ->
            swe_set_ephe_path(this.ephePath)
            val sunPosi = swe_calc_ut(jd, SE_SUN, SEFLG_SWIEPH)
            swe_close()
            if(sunPosi.serr.isNotEmpty() || sunPosi.rc < -0) throw CallException("swe_calc_ut()错误。${sunPosi.serr}")
            mod180(sunPosi.xx[0] - swe_degnorm(monthNum * 30 + 255 + 15.0))
        }

        val solarTerm1Time = getUT8DateTimeFromJd(solarTermJd1)
        this.solarTermSecond =  SolarTermData(SolarTermNames[monthNum * 2 + 1],
            "${solarTerm1Time.year}-${solarTerm1Time.month}-${solarTerm1Time.day} ${solarTerm1Time.hour}:" +
                    "${solarTerm1Time.min}:${solarTerm1Time.sec.roundToInt()}")



    }

    /**
     * 计算某一年冬至开始的连续25个节气
     * 第25个节气=下一年冬至
     * @param year
     * 冬至点所在年份
     */
    private fun get25SolarTermJds(year :Int) :List<Double>{
        val jds = mutableListOf<Double>()

        // 计算前一年冬至点jd
        val jd = swe_julday(year, 12, 20, 0.0, SE_GREG_CAL)
        val dongZhiJd = newtonIteration(jd){jd ->
            swe_set_ephe_path(this.ephePath)
            val sunPosi = swe_calc_ut(jd, SE_SUN, SEFLG_SWIEPH)
            swe_close()
            if(sunPosi.serr.isNotEmpty() || sunPosi.rc < -0) throw CallException("swe_calc_ut()错误。${sunPosi.serr}")
            mod180(sunPosi.xx[0] - 270)
        }
        jds.add(dongZhiJd)
        //计算从此年冬至到下一年冬至的25个节所的jd(utc) ,第25个节气=下一年冬至
        for(i in 1..24){
            // 每个节气大约差15天，因此将前一节气的jd + 15作为迭代初值，jds[i-1] + 15
            var angle = 270 + i * 15
            if(angle >= 360) angle -= 360
            val jd = newtonIteration(jds[i-1] + 15 ){jd ->
                swe_set_ephe_path(this.ephePath)
                val sunPosi = swe_calc_ut(jd, SE_SUN, SEFLG_SWIEPH)
                swe_close()
                if(sunPosi.serr.isNotEmpty() || sunPosi.rc < -0) throw CallException("swe_calc_ut()错误。${sunPosi.serr}")
                mod180(sunPosi.xx[0] - angle)
            }
            jds.add(jd)
        }
        return jds.toList()
    }

    /**
     * 计算从某一年冬至开始的连续15个新月
     * @param jd
     * 冬至点的儒略日
     */
    private fun get15NewMoonJDs(jd :Double) :List<Double>{
        val moonJds = mutableListOf<Double>()

        // 如果冬至点在满月之后会得到下一个合朔
        var shuoDongZhiJd = getNewMoonJD(jd)
        if(shuoDongZhiJd > jd) shuoDongZhiJd = getNewMoonJD(jd - 29.53)
        moonJds.add(shuoDongZhiJd)
        for(i in 1..14){
            moonJds.add(getNewMoonJD(moonJds[i-1] + 29.53))
        }
        return moonJds.toList()
    }

    /**
     * 计算从某年冬至开始连续15个农历月初一的儒略日
     * @param jds
     * 从冬至点所在月份开始，连续15个新月的儒略日
     */
    private fun get15LunarMonthJds(jds :List<Double>) :List<LunarMonthData>{
        val firstDayJds = mutableListOf<LunarMonthData>()
        jds.forEachIndexed { index, it ->
            val t = swe_revjul(it, SE_GREG_CAL)
            val h = t.hour.toInt()
            val mi = ((t.hour - h) * 60).toInt()
            val sec = ((t.hour - h) * 60 - mi) * 60

            // 将新月的jd换算到东八区
            val t8 = swe_utc_time_zone(t.year, t.month, t.day, h, mi, sec, -8.0)
            // 以新月当天00:00:00为初一，计算儒略日
            val t0 = swe_utc_time_zone(t8.year, t8.month, t8.day, 0, 0, 0.0, 8.0)
            val jd = swe_julday(t0.year, t0.month, t0.day, t0.hour + t0.min / 60.0 + t0.sec / 3600.0, SE_GREG_CAL)
            var n = (index + 11) % 12
            if(n == 0)n=12
            firstDayJds.add(LunarMonthData(n, jd))
        }
        return firstDayJds.toList()
    }

    /**
     * 根据节气计算是否有闰月
     * @param lunarMonth
     * 从前一年冬至开始的15个农历月的信息
     * @param jdsMiddleSolarTerm
     * 从前一年冬至开始的中气的儒略日,最后一中气是此年的冬至
     * 前一年冬至点所在农历月计为m_0
     * 此年冬至点所在农历月之前的一个农历月计为m_1
     * 从m_0数到m_1，如果有13个农历月，则置闰
     */
    private fun calcLeapMonth(lunarMonth :List<LunarMonthData>, jdsMiddleSolarTerm :List<Double>) {
        var n = lunarMonth.filter { jdsMiddleSolarTerm.last() >= it.jd }.size - 1
        if(n == 12) return
        for(i in 0 until  lunarMonth.size - 1){
            var middleSolarTerm = false
            for(j in jdsMiddleSolarTerm.indices) {
                if(lunarMonth[i].jd < jdsMiddleSolarTerm[j] && jdsMiddleSolarTerm[j] < lunarMonth[i+1].jd){
                    middleSolarTerm = true
                    break
                }
            }
            if(!middleSolarTerm){
                lunarMonths[i].isLeap = true
                for (j in i until lunarMonth.size){
                    lunarMonth[j].num--
                    if(lunarMonth[j].num == 0)lunarMonth[j].num +=12
                }
                break
            }
        }
    }




    /**
     * 计算给定jd所在农历月，日月合朔的jd
     * 如果jd在满月之后，迭代值为下一个合朔
     */
    private fun getNewMoonJD(jd :Double) :Double{
        var shuoJd = newtonIteration(jd){ jd ->
            swe_set_ephe_path(this.ephePath)
            val sunPosi = swe_calc_ut(jd, SE_SUN, SEFLG_SWIEPH)
            val moonPosi = swe_calc_ut(jd, SE_MOON, SEFLG_SWIEPH)
            swe_close()
            if(sunPosi.serr.isNotEmpty() || sunPosi.rc < -0) throw CallException("swe_calc_ut()错误。${sunPosi.serr}")
            if(moonPosi.serr.isNotEmpty() || moonPosi.rc < -0) throw CallException("swe_calc_ut()错误。${moonPosi.serr}")

            // 将结果转换到[0, 360)，再将结果转换到[-180, 180]
            // 这样可以确保曲线上日月合朔点是连续的，
            // 如果处理后，曲线两个端点将在满月，如果迭代的jd大于下一个满月点，
            //则会迭代到下一次日月合朔，因此需要时行判断
            //如果迭代值大于初值，则再次迭代
            mod180(swe_degnorm(moonPosi.xx[0] - sunPosi.xx[0]))
        }

//        if(shuoJd > jd){
//            shuoJd = newtonIteration(shuoJd - 29.53 ){ jd ->
//                swe_set_ephe_path(ephePath)
//                val sunPosi = swe_calc_ut(jd, SE_SUN, SEFLG_SWIEPH)
//                val moonPosi = swe_calc_ut(jd, SE_MOON, SEFLG_SWIEPH)
//                swe_close()
//                if(sunPosi.serr.isNotEmpty() || sunPosi.rc < -0) throw CallException("swe_calc_ut()错误。${sunPosi.serr}")
//                if(moonPosi.serr.isNotEmpty() || moonPosi.rc < -0) throw CallException("swe_calc_ut()错误。${moonPosi.serr}")
//                mod180(swe_degnorm(moonPosi.xx[0] - sunPosi.xx[0]))
//            }
//        }
        return shuoJd
    }

    override fun toString(): String {
        return "${this.lunarYear}年${this.lunarMonth}${this.lunarDay} " +
                "${this.lunarMonthGanZhi}月 ${this.lunarDayGanZhi}日 ${this.timeGanZhi}时 ${this.solarTermFirst} ${solarTermSecond}"
    }
}

