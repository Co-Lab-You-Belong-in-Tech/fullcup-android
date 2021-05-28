package com.cerdenia.android.fullcup.data.model

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cerdenia.android.fullcup.util.DATE_PATTERN
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class ActivityLog(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val category: String,
    @SuppressLint("SimpleDateFormat")
    val date: String = SimpleDateFormat(DATE_PATTERN).format(Date()),
    var isDone: Boolean = false,
) : Serializable