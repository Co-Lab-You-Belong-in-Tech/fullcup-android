package com.cerdenia.android.fullcup.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cerdenia.android.fullcup.data.model.EventIdPair
import com.cerdenia.android.fullcup.data.model.Reminder

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addReminder(vararg reminder: Reminder)

    @Query("SELECT * FROM reminder ORDER BY name ASC")
    fun getReminders(): LiveData<List<Reminder>>

    @Query("SELECT * FROM reminder")
    fun getRemindersSync(): List<Reminder>

    @Update
    fun updateReminder(vararg reminder: Reminder)

    @Query("UPDATE reminder SET serverId = :id WHERE name = :name")
    fun addIdToReminder(id: String, name: String)

    @Query("DELETE FROM reminder WHERE name IN (:name)")
    fun deleteReminderByName(vararg name: String)

    @Query("DELETE FROM reminder WHERE name NOT IN (:reminderNames)")
    fun deleteLeftoverRemindersByName(reminderNames: List<String>)

    @Transaction
    fun addIdsToReminders(events: List<EventIdPair>) {
        events.forEach { event -> addIdToReminder(event.id, event.name) }
    }

    @Transaction
    fun updateReminderSet(toCreate: Array<Reminder>, toDelete: Array<String>) {
        addReminder(*toCreate)
        deleteReminderByName(*toDelete)
    }
}