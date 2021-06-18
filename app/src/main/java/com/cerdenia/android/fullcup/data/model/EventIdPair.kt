package com.cerdenia.android.fullcup.data.model

import com.google.gson.annotations.SerializedName

data class EventIdPair(
    val id: String,
    @SerializedName("summary")
    val name: String
)