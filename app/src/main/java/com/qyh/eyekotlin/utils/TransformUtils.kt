package com.qyh.eyekotlin.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author 邱永恒
 *
 * @playDuration 2018/3/28  13:03
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
    fun playDuration(duration: Int): String {
        return "${df.format(duration.div(60))}'${df.format(duration % 60)}''"
    }

    /**
     * 日期转换
     */
    fun formatDate(date: Long): String {
       return dateFormat.format(date)
    }
}