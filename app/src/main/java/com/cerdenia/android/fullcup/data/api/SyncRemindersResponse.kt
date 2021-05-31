package com.cerdenia.android.fullcup.data.api

import com.cerdenia.android.fullcup.data.model.EventIdPair
import com.google.gson.annotations.SerializedName

class SyncRemindersResponse {
    @SerializedName("results")
    lateinit var currentEvents: List<EventIdPair>
}