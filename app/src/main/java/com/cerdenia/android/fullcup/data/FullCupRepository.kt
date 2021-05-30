package com.cerdenia.android.fullcup.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.cerdenia.android.fullcup.data.api.SyncEventsResponse
import com.cerdenia.android.fullcup.data.api.SyncEventsBody
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

    // [START] API methods
    fun syncEventsWithCalendar(reminders: List<Reminder>) {
        val events = reminders.map { it.toCalendarEvent() }
        val idsForDeletion = FullCupPreferences.googleEventIds
            .filter { id ->
                val currentEventIds = events.map { it.googleId }
                !currentEventIds.contains(id)
            }
        val body = SyncEventsBody(events, idsForDeletion)
        val request = webService.syncEvents(body)

        request.enqueue(object : Callback<SyncEventsResponse> {
            override fun onResponse(
                call: Call<SyncEventsResponse>,
                response: Response<SyncEventsResponse>
            ) {
                Log.d(TAG, "Successfully called API!")
                // Response body will contain a list of newly created event IDs
                // on the user's calendar, each paired with a summary of the event.
                val currentEvents = response.body()?.currentEvents ?: emptyList()
                FullCupPreferences.googleEventIds = currentEvents.map { it.id }

                executor.execute {
                    reminderDao.addIdsToReminders(currentEvents)
                }
            }

            override fun onFailure(call: Call<SyncEventsResponse>, t: Throwable) {
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
    fun getLogByDate(date: String) = logDao.getLogsByDate(date)

    fun addAndDeleteActivityLogs(toAdd: List<ActivityLog>, toDelete: List<ActivityLog>) {
        executor.execute { logDao.addAndDeleteActivityLogs(toAdd, toDelete) }
    }

    fun addOrUpdateDailyLog(log: DailyLog) {
        executor.execute { logDao.addOrUpdateDailyLog(log) }
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