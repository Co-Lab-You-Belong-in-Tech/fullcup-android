package com.cerdenia.android.fullcup.data

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("summary")
    val summary: String,
    @SerializedName("start")
    val start: String,
    @SerializedName("end")
    val end: String,
    //@SerializedName("creator.email")
    //val creator: String,
)