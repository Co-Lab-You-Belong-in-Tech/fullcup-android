package com.cerdenia.android.fullcup.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cerdenia.android.fullcup.data.model.ActivityLog
import com.cerdenia.android.fullcup.data.model.DailyLog
import com.cerdenia.android.fullcup.data.model.SummaryLog

@Dao
interface LogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addActivityLog(vararg log: ActivityLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSummaryLog(log: SummaryLog)

    @Transaction
    @Query("SELECT * FROM summaryLog WHERE date = :date")
    fun getLogsByDate(date: String): LiveData<DailyLog?>

    @Update
    fun updateActivityLog(vararg log: ActivityLog)

    @Update
    fun updateSummaryLog(log: SummaryLog)

    @Transaction
    fun addOrUpdateDailyLog(log: DailyLog) {
        addActivityLog(*log.activities.toTypedArray())
        addSummaryLog(log.summary)
    }
}