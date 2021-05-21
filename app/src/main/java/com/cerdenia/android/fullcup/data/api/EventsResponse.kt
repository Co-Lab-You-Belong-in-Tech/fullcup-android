package com.cerdenia.android.fullcup.data.api

import com.cerdenia.android.fullcup.data.Calendar
import com.cerdenia.android.fullcup.data.Event
import com.google.gson.annotations.SerializedName

class EventsResponse {
    @SerializedName("data")
    lateinit var events: List<Event>
}