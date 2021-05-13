package pub.teanote.lunarcalendar

import pub.teanote.lunarcalendar.exception.CallException
import pub.teanote.sweph.*
import kotlin.math.abs


fun main(args: Array<String>) {
    val year = 2020
    val ephePath = "/Users/lzh/php/sweph"
    val a = LunarCalendar(1280, 12, 7, 22,0, ephePath = ephePath)
    println(a)


//    println(a.lunarYear)
//    println(a.lunarMonth)
//    println(a.lunarDay)
}