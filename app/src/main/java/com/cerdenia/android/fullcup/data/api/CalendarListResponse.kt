package com.cerdenia.android.fullcup.data.api

import com.cerdenia.android.fullcup.data.Calendar
import com.google.gson.annotations.SerializedName

class CalendarListResponse {
    @SerializedName("data")
    lateinit var calendars: List<Calendar>
}