package com.cerdenia.android.fullcup.util

import java.lang.IllegalArgumentException

object Utils {
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
}