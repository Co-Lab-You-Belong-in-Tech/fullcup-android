package com.cerdenia.android.fullcup.data.model

import java.io.Serializable

data class Reminder(
    val category: String,
    var time: String? = null,
    var days: String? = null
) : Serializable {
    fun isSet(): Boolean {
        return (time !== null && days !== null)
    }
}