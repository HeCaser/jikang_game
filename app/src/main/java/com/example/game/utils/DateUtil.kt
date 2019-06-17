package com.example.game.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by htp on 2018/4/12.
 */
class DateUtil {

    companion object {

        fun getPatternByYearMonthDay(split: String) = "yyyy${split}MM${split}dd"

        fun parseDate(pattern: String, source: String): Calendar {
            var date = if (source.isEmpty()) {
                Date()
            } else {
                SimpleDateFormat(pattern)
                    .parse(source)
            }


            return Calendar.getInstance().apply {
                time = date
            }
        }

        fun dateFormat(pattern: String, date: String): String {
            var d = date.toLongOrNull()

            return if (d != null) {
                dateFormat(pattern, d)
            } else {
                dateFormat(pattern)
            }
        }

        fun dateFormat(pattern: String, date: Long): String {
            var d = if (date.toString().length == 10) {
                date * 1000L
            } else {
                date
            }

            return dateFormat(pattern, Date(d))
        }

        fun dateFormat(pattern: String = "yyyy-MM-dd", date: Date = Date()): String {
            return SimpleDateFormat(pattern).format(date)
        }


        fun getMinuteAndSecondByMillisecond(ml: Long): String {
            var res = ""
            var second = (ml / 1000) % 60
            var minute = ml / 1000 / 60
            println("ml=$ml  se=$second mi=$minute")
            val minuteS = if (minute >= 10) minute.toString() else "0$minute"
            val secondS = if (second >= 10) second.toString() else "0$second"
            res = "$minuteS 分 : $secondS 秒"

            return res
        }
    }
}