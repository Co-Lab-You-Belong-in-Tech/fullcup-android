package com.cerdenia.android.fullcup.data.model

import com.google.gson.annotations.SerializedName

data class Calendar(
    @SerializedName("id")
    val id: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("description")
    val description: String?
)
