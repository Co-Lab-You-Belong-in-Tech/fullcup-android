package com.cerdenia.android.fullcup.data

import androidx.lifecycle.LiveData
import com.cerdenia.android.fullcup.data.api.WebService
import com.cerdenia.android.fullcup.data.local.db.FullCupDatabase
import com.cerdenia.android.fullcup.data.model.ActivityLog
import com.cerdenia.android.fullcup.data.model.DailyLog
import com.cerdenia.android.fullcup.data.model.Reminder
import java.util.concurrent.Executors

class FullCupRepository private constructor(
    database: FullCupDatabase,
    private val webService: WebService
) {
    private val reminderDao = database.reminderDao()
    private val logDao = database.logDao()
    private val executor = Executors.newSingleThreadExecutor()

    // [START] API methods
    fun addRemindersToCalendar(reminders: List<Reminder>) {
        // TODO: API request to add events to user's calendar
    }
    // [END] API methods

    // [START] Reminder methods
    fun getReminders(): LiveData<List<Reminder>> = reminderDao.getReminders()

    fun updateReminder(reminder: Reminder) {
        executor.execute { reminderDao.updateReminder(reminder) }
    }

    fun updateReminderSet(toCreate: List<Reminder>, categoriesToDelete: List<String>) {
        executor.execute { reminderDao.updateReminderSet(
            toCreate.toTypedArray(),
            categoriesToDelete.toTypedArray()
        ) }
    }
    // [END] Reminder methods

    // [START] Daily Log methods
    fun getLogByDate(date: String) = logDao.getLogsByDate(date)

    fun addActivityLog(vararg log: ActivityLog) {
        executor.execute { logDao.addActivityLog(*log) }
    }

    fun addOrUpdateDailyLog(log: DailyLog) {
        executor.execute { logDao.addOrUpdateDailyLog(log) }
    }

    fun deleteActivityLog(vararg log: ActivityLog) {
        executor.execute { logDao.deleteActivityLog(*log) }
    }
    // [END] Daily Log methods

    companion object {
        private var INSTANCE: FullCupRepository? = null

        fun init(database: FullCupDatabase, webService: WebService) {
            if (INSTANCE == null) INSTANCE = FullCupRepository(database, webService)
        }

        fun getInstance(): FullCupRepository {
            return INSTANCE ?: throw IllegalStateException("Repo must be initialized")
        }
    }
}