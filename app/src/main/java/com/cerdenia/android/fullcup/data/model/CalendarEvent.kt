package com.cerdenia.android.fullcup.data.model

import java.util.*

// Container for Reminder data to be sent to API
data class CalendarEvent(
    val googleId: String?,
    val summary: String,
    val start: String,
    val durationInMins: Int,
    val timeZone: String = TimeZone.getDefault().id,
    val recurrence: String
)