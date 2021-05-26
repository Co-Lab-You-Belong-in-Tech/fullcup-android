package com.cerdenia.android.fullcup.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cerdenia.android.fullcup.data.model.DailyLog
import java.util.*

@Dao
interface LogDao {
    @Insert
    fun addLog(log: DailyLog)

    @Query("SELECT * FROM dailylog WHERE date = :date")
    fun getLog(date: Date)

    @Update
    fun updateLog(log: DailyLog)
}