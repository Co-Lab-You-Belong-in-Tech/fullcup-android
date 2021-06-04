package com.cerdenia.android.fullcup.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.cerdenia.android.fullcup.data.api.SyncRemindersBody
import com.cerdenia.android.fullcup.data.api.SyncRemindersResponse
import com.cerdenia.android.fullcup.data.api.WebService
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.data.local.db.FullCupDatabase
import com.cerdenia.android.fullcup.data.model.ActivityLog
import com.cerdenia.android.fullcup.data.model.DailyLog
import com.cerdenia.android.fullcup.data.model.Reminder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class FullCupRepository private constructor(
    database: FullCupDatabase,
    private val webService: WebService
) {
    private val reminderDao = database.reminderDao()
    private val logDao = database.logDao()
    private val executor = Executors.newSingleThreadExecutor()

    init {
        // In case any Reminder objects are left
        // in the DB after a name change:
        executor.execute {
            val reminders = reminderDao.getRemindersSync()
            val names = reminders.map { it. name }
            reminderDao.deleteLeftoverRemindersByName(names)
        }
    }

    // [START] API methods
    fun syncRemindersWithCalendar(reminders: List<Reminder>) {
        reminders.forEach { reminder -> reminder.setStartDateTime() }
        val idsForDeletion = FullCupPreferences.serverIds
            .filter { id ->
                val currentEventIds = reminders.map { it.serverId }
                !currentEventIds.contains(id)
            }

        val body = SyncRemindersBody(reminders, idsForDeletion)
        val request = webService.syncEvents(body)

        request.enqueue(object : Callback<SyncRemindersResponse> {
            override fun onResponse(
                call: Call<SyncRemindersResponse>,
                response: Response<SyncRemindersResponse>
            ) {
                Log.d(TAG, "Successfully called API!")
                // Response body will contain a list of newly created event IDs
                // on the user's calendar, each paired with a summary of the event.
                val currentEvents = response.body()?.currentEvents ?: emptyList()
                // Save set of event IDs from server and map to existing reminders in DB.
                // If an entry in DB includes a server ID, we know it has been synced.
                FullCupPreferences.serverIds = currentEvents.map { it.id }
                executor.execute { reminderDao.addIdsToReminders(currentEvents) }
            }

            override fun onFailure(call: Call<SyncRemindersResponse>, t: Throwable) {
                Log.d(TAG, "Something went wrong", t)
            }
        })
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
    fun getLogsByDate(date: String) = logDao.getLogsByDate(date)

    fun addOrUpdateDailyLog(log: DailyLog) {
        executor.execute { logDao.addOrUpdateDailyLog(log) }
    }

    fun initDailyLog(dailyLog: DailyLog, stringDate: String) {
        executor.execute {
            val existingLog = logDao.getLogsByDateSync(stringDate)
            if (existingLog == null) logDao.addOrUpdateDailyLog(dailyLog)
        }
    }

    fun addAndDeleteActivityLogs(toAdd: List<ActivityLog>, toDelete: List<ActivityLog>) {
        executor.execute { logDao.addAndDeleteActivityLogs(toAdd, toDelete) }
    }
    // [END] Daily Log methods

    companion object {
        private const val TAG = "FullCupRepository"
        private var INSTANCE: FullCupRepository? = null

        fun init(database: FullCupDatabase, webService: WebService) {
            if (INSTANCE == null) INSTANCE = FullCupRepository(database, webService)
        }

        fun getInstance(): FullCupRepository {
            return INSTANCE ?: throw IllegalStateException("Repo must be initialized")
        }
    }
}