package com.cerdenia.android.fullcup.data.api

import com.cerdenia.android.fullcup.data.model.CalendarEvent

data class SyncEventsBody(
    val events: List<CalendarEvent>,
    val idsForDeletion: List<String>
)