package com.cerdenia.android.fullcup.data.model

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cerdenia.android.fullcup.DAILY
import com.cerdenia.android.fullcup.DATE_PATTERN
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Reminder(
    @PrimaryKey val category: String,
    var time: String? = null,
    var days: String? = null,
    var durationInMins: Int = 60,
    val googleId: String? = null,
    val dateAdded: Date = Date(),
    var startDateTime: String? = null
) : Serializable {
    val isSet: Boolean get() = time !== null && days !== null

    @SuppressLint("SimpleDateFormat")
    fun toCalendarEvent(): CalendarEvent {
        val date = SimpleDateFormat(DATE_PATTERN).format(Date())
        return CalendarEvent(
            googleId = googleId,
            summary = category,
            start = "$date ${time}:00",
            durationInMins = durationInMins,
            recurrence = days ?: DAILY
        )
    }
}