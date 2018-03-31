package com.qyh.eyekotlin.utils

import java.util.*

/**
 * @author 邱永恒
 *
 * @playDuration 2018/3/30  13:53
 *
 * @desc ${TODD}
 *
 */
object CalendarUtils {
    private val list by lazy { arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday") }

    fun getToday(): String {
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        var index = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (index < 0) {
            index = 0
        }
        return list[index]
    }
}