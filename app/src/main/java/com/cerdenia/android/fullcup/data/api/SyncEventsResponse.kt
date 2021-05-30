package com.cerdenia.android.fullcup.data.api

import com.cerdenia.android.fullcup.data.model.EventIdWithSummary
import com.google.gson.annotations.SerializedName

class SyncEventsResponse {
    @SerializedName("results")
    lateinit var currentEvents: List<EventIdWithSummary>
}