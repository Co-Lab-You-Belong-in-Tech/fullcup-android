package com.cerdenia.android.fullcup.data.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

// Container for all logs with the same date.
data class DailyLog(
    @Embedded val summary: SummaryLog,
    @Relation(
        parentColumn = "date",
        entityColumn = "date"
    )
    val activities: MutableList<ActivityLog>
) : Serializable