package com.cerdenia.android.fullcup.data.api

import com.cerdenia.android.fullcup.data.model.BusyTime
import com.google.gson.annotations.SerializedName

class BusyTimesResponse {
    @SerializedName("data")
    lateinit var busyTimes: List<BusyTime>
}