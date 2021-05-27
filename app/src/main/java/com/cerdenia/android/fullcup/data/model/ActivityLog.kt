package com.cerdenia.android.fullcup.data.model

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class ActivityLog(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val category: String,
    val date: String = SimpleDateFormat("yyyy-MM-dd").format(Date()),
    var isDone: Boolean = false,
) : Serializable