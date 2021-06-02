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

    @Transaction
    @Query("SELECT * FROM summaryLog WHERE date = :date")
    fun getLogsByDateSync(date: String): DailyLog?

    @Delete
    fun deleteActivityLog(vararg log: ActivityLog)

    @Transaction
    fun addAndDeleteActivityLogs(toAdd: List<ActivityLog>, toDelete: List<ActivityLog>) {
        deleteActivityLog(*toDelete.toTypedArray())
        addActivityLog(*toAdd.toTypedArray())
    }

    @Transaction
    fun addOrUpdateDailyLog(log: DailyLog) {
        addActivityLog(*log.activities.toTypedArray())
        addSummaryLog(log.summary)
    }
}