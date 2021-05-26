package com.cerdenia.android.fullcup.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.cerdenia.android.fullcup.data.local.db.FullCupDatabase
import com.cerdenia.android.fullcup.data.model.Reminder
import java.util.concurrent.Executors

class FullCupRepository private constructor(
    context: Context,
    db: FullCupDatabase
) {
    private val reminderDao = db.reminderDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun addReminder(reminder: Reminder) {
        executor.execute { reminderDao.addReminder(reminder) }
    }

    fun getReminders(): LiveData<List<Reminder>> = reminderDao.getReminders()

    fun updateReminder(reminder: Reminder) {
        executor.execute { reminderDao.updateReminder(reminder) }
    }

    fun deleteReminder(reminder: Reminder) {
        executor.execute { reminderDao.deleteReminder(reminder) }
    }

    fun updateReminderSet(toCreate: List<Reminder>, categoriesToDelete: List<String>) {
        executor.execute { reminderDao.updateReminderSet(
            toCreate.toTypedArray(),
            categoriesToDelete.toTypedArray()
        ) }
    }

    companion object {
        private var INSTANCE: FullCupRepository? = null

        fun init(context: Context, db: FullCupDatabase) {
            if (INSTANCE == null) {
                INSTANCE = FullCupRepository(context, db)
            }
        }

        fun getInstance(): FullCupRepository {
            return INSTANCE ?: throw IllegalStateException("Repository must be initialized")
        }
    }
}