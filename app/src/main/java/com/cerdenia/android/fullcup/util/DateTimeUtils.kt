package com.cerdenia.android.fullcup.util

import com.cerdenia.android.fullcup.ui.dialog.SetReminderFragment
import java.lang.IllegalArgumentException

object DateTimeUtils {
    // Accepts time in HH:mm minute format.
    fun to12HourFormat(stringTime: String): String {
        if (!stringTime.contains(":") || stringTime.length != 5) {
            throw IllegalArgumentException("Invalid string time $stringTime, must be HH:mm.")
        }

        val hour = stringTime.substringBefore(":").toInt()
        val minutes = stringTime.substringAfter(":")

        return when (hour) {
            0 -> "12:$minutes AM"
            in 1..11 -> "$hour:$minutes AM"
            12 -> "12:$minutes PM"
            in 13..23 -> "${hour - 12}:$minutes PM"
            else -> throw IllegalStateException("Hour must not be more than 23.")
        }
    }

    // Accepts day name in EEE format. Returns whether day is a weekday or weekend.
    fun toTypeOfDay(dayString: String): String {
        val weekdays = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri")
        val weekends = arrayOf("Sat", "Sun")
        return when {
            weekdays.contains(dayString) -> SetReminderFragment.WEEKDAYS
            weekends.contains(dayString) -> SetReminderFragment.WEEKENDS
            else -> "ERROR"
        }
    }
}