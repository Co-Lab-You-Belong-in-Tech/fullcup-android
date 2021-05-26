package com.cerdenia.android.fullcup.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cerdenia.android.fullcup.data.model.Reminder

@Dao
interface ReminderDao {
    @Insert
    fun addReminder(reminder: Reminder)

    @Query("SELECT * FROM reminder")
    fun getReminders(): LiveData<List<Reminder>>

    @Update
    fun updateReminder(reminder: Reminder)

    @Delete
    fun deleteReminder(reminder: Reminder)
}