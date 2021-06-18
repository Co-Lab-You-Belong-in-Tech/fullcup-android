package com.cerdenia.android.fullcup.data.model

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cerdenia.android.fullcup.DATE_PATTERN
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

// Represents an event that can be synced with the backend server.
@Entity
data class Reminder(
    @PrimaryKey val name: String,
    var time: String? = null,
    var recurrence: String? = null,
    var durationInMins: Int = 60,
    var startDateTime: String? = null,
    var timeZone: String = TimeZone.getDefault().id,
    var serverId: String? = null,
) : Serializable {
    val isSet: Boolean get() = time !== null && recurrence !== null

    // Should be called before passing data to server.
    @SuppressLint("SimpleDateFormat")
    fun setStartDateTime() {
        val date = SimpleDateFormat(DATE_PATTERN).format(Date())
        startDateTime = "$date ${time}:00"
        timeZone = TimeZone.getDefault().id
    }
}