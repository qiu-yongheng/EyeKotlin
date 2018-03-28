package com.qyh.eyekotlin.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author 邱永恒
 *
 * @time 2018/3/28  13:03
 *
 * @desc ${TODD}
 *
 */
object TransformUtils {
    private val df by lazy { DecimalFormat("##") }
    private val dateFormat by lazy { SimpleDateFormat("MM-dd", Locale.CHINA) }
    /**
     * 秒转换时间
     */
    fun time(duration: Int): String {
        val m = duration.div(60)
        val s = duration % 60
        return "${df.format(m)}'${df.format(s)}''"
    }

    /**
     * 日期转换
     */
    fun date(date: Long): String {
       return dateFormat.format(date)
    }
}