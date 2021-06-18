package com.cerdenia.android.fullcup.util

import android.content.res.Resources
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.WEEKDAY
import com.cerdenia.android.fullcup.WEEKEND
import com.cerdenia.android.fullcup.data.model.DateTimeBreakdown
import java.util.*

object DateTimeUtils {
    // Accepts time in HH:mm format.
    fun to12HourFormat(timeString: String): String {
        if (!timeString.contains(":") || timeString.length != 5) {
            throw IllegalArgumentException("Invalid string time $timeString, must be HH:mm.")
        }

        val hour = getHourOfDay(timeString)
        val minutes = timeString.substringAfter(":")

        return when (hour) {
            0 -> "12:$minutes AM"
            in 1..11 -> "$hour:$minutes AM"
            12 -> "12:$minutes PM"
            in 13..23 -> "${hour - 12}:$minutes PM"
            else -> throw IllegalStateException("Hour must not be more than 23.")
        }
    }

    fun to24HourFormat(hour: Int, minutes: Int): String {
        val formattedHour = if (hour in 0..9) "0$hour" else hour.toString()
        val formattedMinutes = if (minutes in 0..9) "0$minutes" else minutes.toString()
        return "$formattedHour:$formattedMinutes"
    }

    // Accepts time in HH:mm format.
    fun getGreeting(resources: Resources, timeString: String): String {
        return when (timeString.substringBefore(":").toInt()) {
            in 0..11 -> R.string.good_morning
            12 -> R.string.good_afternoon
            in 13..17 -> R.string.good_afternoon
            in 18..23 -> R.string.good_evening
            else -> R.string.good_day
        }.run { resources.getString (this) }
    }

    // Accepts day name in EEE format. Returns whether day is a weekday or weekend.
    fun toTypeOfDay(dayString: String): String {
        val weekdays = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri")
        val weekends = arrayOf("Sat", "Sun")
        return when {
            weekdays.contains(dayString) -> WEEKDAY
            weekends.contains(dayString) -> WEEKEND
            else -> "ERROR"
        }
    }

    // Accepts a year, month, and date as Int. Returns date in YYYY-MM-dd format.
    // Month is counted from 0, e.g. January is 0, February is 1, etc.
    fun toDateString(y: Int, m: Int, d: Int): String {
        val month: String = if (m + 1 in 0..9) "0${m + 1}" else "${m + 1}"
        val day: String = if (d in 0..9) "0$d" else "$d"
        return "$y-$month-$day"
    }

    // Accepts a string date in YYYY-MM-dd format.
    fun toDate(dateString: String): Date {
        val year = getYear(dateString)
        val month = getMonth(dateString)
        val day = getDayOfMonth(dateString)
        val timeInMillis: Long = Calendar.getInstance().run {
            set(year, month - 1, day)
            timeInMillis
        }
        return Date(timeInMillis)
    }

    fun breakdown(dateString: String, timeString: String): DateTimeBreakdown {
        return DateTimeBreakdown(
            getYear(dateString),
            getMonth(dateString),
            getDayOfMonth(dateString),
            getHourOfDay(timeString),
            getMinutes(timeString)
        )
    }

    fun breakdown(dateTimeString: String): DateTimeBreakdown {
        val dateString = dateTimeString.substringBefore(" ")
        val timeString = dateTimeString
            .substringAfter(" ")
            .substringBeforeLast(":")
        return breakdown(dateString, timeString)
    }

    private fun getYear(dateString: String): Int {
        return dateString
            .substringBefore("-")
            .toInt()
    }

    private fun getMonth(dateString: String): Int {
        return dateString
            .substringAfter("-")
            .substringBefore("-")
            .toInt()
            .minus(1)
    }

    private fun getDayOfMonth(dateString: String): Int {
        return dateString
            .substringAfter("-")
            .substringAfter("-")
            .toInt()
    }

    private fun getHourOfDay(timeString: String): Int {
        return timeString
            .substringBefore(":")
            .toInt()
    }

    private fun getMinutes(timeString: String): Int {
        return timeString.substringAfter(":").toInt()
    }
}