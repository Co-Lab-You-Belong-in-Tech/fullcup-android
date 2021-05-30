package com.cerdenia.android.fullcup.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cerdenia.android.fullcup.data.model.EventIdWithSummary
import com.cerdenia.android.fullcup.data.model.Reminder

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addReminder(vararg reminder: Reminder)

    @Query("SELECT * FROM reminder ORDER BY category ASC")
    fun getReminders(): LiveData<List<Reminder>>

    @Update
    fun updateReminder(vararg reminder: Reminder)

    @Delete
    fun deleteReminder(vararg reminder: Reminder)

    @Query("DELETE FROM reminder WHERE category IN (:category)")
    fun deleteReminderByCategory(vararg category: String)

    @Query("UPDATE reminder SET googleId = :id WHERE category = :category")
    fun addIdToReminder(id: String, category: String)

    @Transaction
    fun addIdsToReminders(currentEvents: List<EventIdWithSummary>) {
        currentEvents.forEach { addIdToReminder(it.id, it.summary) }
    }

    @Transaction
    fun updateReminderSet(toCreate: Array<Reminder>, toDelete: Array<String>) {
        addReminder(*toCreate)
        deleteReminderByCategory(*toDelete)
    }
}