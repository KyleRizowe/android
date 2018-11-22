package com.fuel4media.carrythistoo.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Created by craterzone on 8/11/16.
 */

object DateFormatter {

    private val SEC_IN_A_DAY = (24 * 60 * 60).toLong()

    val epochDateSec: Long
        get() = Calendar.getInstance(TimeZone.getTimeZone("GMT")).timeInMillis / 1000 / SEC_IN_A_DAY * SEC_IN_A_DAY

    val epochSecOfDay: Int
        get() {
            val c = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            return c.get(Calendar.HOUR_OF_DAY) * 60 * 60 + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND)
        }

    fun getCurrentDate(formate: String): String {
        val sdf = SimpleDateFormat(formate, Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val date = Date(Calendar.getInstance().timeInMillis)
        return sdf.format(date)
    }

    fun getDateFromSecs(secs: Long, formate: String): String {
        val sdf = SimpleDateFormat(formate, Locale.US)
        val date = Date(secs * 1000)
        return sdf.format(date)
    }

    fun getDateFromTimestamp(timestamp: Long, formate: String): String {
        val sdf = SimpleDateFormat(formate, Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val date = Date(timestamp)
        return sdf.format(date)
    }

    fun convertMillisToHrs(millis: Long): Int {
        return millis.toInt() / (1000 * 3600)
    }


}
