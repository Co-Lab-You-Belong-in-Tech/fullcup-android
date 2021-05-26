package com.cerdenia.android.fullcup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Reminder(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val dateAdded: Date = Date(),
    val category: String,
    var time: String? = null,
    var days: String? = null
) : Serializable {
    val isSet: Boolean get() = time !== null && days !== null
}