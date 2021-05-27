package com.cerdenia.android.fullcup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class SummaryLog(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var content: String = "",
    val date: String = SimpleDateFormat("yyyy-MM-dd").format(Date())
) : Serializable