package com.cerdenia.android.fullcup.ui.viewmodel

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import app.futured.donut.DonutSection
import com.cerdenia.android.fullcup.DAILY
import com.cerdenia.android.fullcup.DATE_PATTERN
import com.cerdenia.android.fullcup.DAY_NAME_PATTERN
import com.cerdenia.android.fullcup.data.FullCupRepository
import com.cerdenia.android.fullcup.data.model.ActivityLog
import com.cerdenia.android.fullcup.data.model.ColoredActivity
import com.cerdenia.android.fullcup.data.model.DailyLog
import com.cerdenia.android.fullcup.data.model.SummaryLog
import com.cerdenia.android.fullcup.util.DateTimeUtils
import com.cerdenia.android.fullcup.util.ext.filterDone
import com.cerdenia.android.fullcup.util.ext.names
import com.cerdenia.android.fullcup.util.ext.notIn
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "HomeViewModel"

// Data source for HomeFragment.
@SuppressLint("SimpleDateFormat")
class HomeViewModel(
    private val repo: FullCupRepository = FullCupRepository.getInstance()
) : ViewModel() {

    val remindersLive = repo.getReminders()
    val date = Date()

    private val typeOfDay = SimpleDateFormat(DAY_NAME_PATTERN).format(date).run {
        DateTimeUtils.toTypeOfDay(this)
    }

    // Colors for identifying donut sections.
    private val colors = listOf(
        "#F9C06E", "#E1937B", "#E17EA3", "#739B00", "#766CE0", "#E6B5EA95"
    ).map { hex -> Color.parseColor(hex) }.shuffled()

    // Get only reminders relevant to current day.
    // e.g. if it is a weekday, get only reminders whose
    // {recurrence} prop is "weekday" or "daily"
    fun getActivitiesOfDay(): List<String> {
        return remindersLive.value
            ?.filter { it.recurrence == typeOfDay || it.recurrence == DAILY }
            ?.map { it.name }
            ?: listOf()
    }

    // Pair activities of the day with pre-defined colors.
    val coloredActivities get() = getActivitiesOfDay()
        .mapIndexed { i, activity -> ColoredActivity(activity, colors[i]) }

    // Reminder data from DB is not directly exposed to view.
    private val stringDate = SimpleDateFormat(DATE_PATTERN).format(date)
    private val dbDailyLogLive: LiveData<DailyLog?> = repo.getLogsByDate(stringDate)

    // Instead, these are exposed:
    val dailyLogLive = MediatorLiveData<DailyLog?>()
    val donutDataLive = MediatorLiveData<List<DonutSection>>()

    init {
        val dailyLog = createNewDailyLog()
        repo.initDailyLog(dailyLog, stringDate)
    }

    private fun createNewDailyLog(): DailyLog {
        val activities = getActivitiesOfDay()
        return DailyLog(
            summary = SummaryLog(),
            activities = activities.map { activity ->
                ActivityLog(name = activity)
            } as MutableList<ActivityLog>
        )
    }

    fun onRemindersFetched() {
        // Need to prevent MediatorLiveData sources from being added
        // more than once if this function is called unexpectedly.
        donutDataLive.removeSource(dailyLogLive)
        dailyLogLive.removeSource(dbDailyLogLive)

        // Validate data before exposing to view. We want user-selected
        // activities of the day to match ActivityLogs stored in DB.
        dailyLogLive.addSource(dbDailyLogLive) { source ->
            var activitiesToDelete: List<ActivityLog> = listOf()
            val activitiesToAdd: MutableList<ActivityLog> = mutableListOf()

            val activities: Set<String> = getActivitiesOfDay().toSet()
            var loggedActivities: Set<String> = source?.activities.names().toSet()

            if (activities !== loggedActivities) {
                // First, delete logs for categories that a user has deselected.
                activitiesToDelete = source?.activities.notIn(activities)
                source?.activities?.removeAll(activitiesToDelete)

                // Recalculate logged activities since source has changed.
                loggedActivities = source?.activities.names().toSet()

                // Then, create new logs for activities that have been newly selected.
                activities.forEach { activity ->
                    if (!loggedActivities.contains(activity)) {
                        val activityLog = ActivityLog(name = activity)
                        activitiesToAdd.add(activityLog)
                    }
                }
            }

            // Update time on each activity log based on set reminders.
            source?.activities?.forEach { activity ->
                val reminder = remindersLive.value?.find { it.name == activity.name }
                reminder?.time?.let { activity.time = it }
            }

            dailyLogLive.value = source
            if (activitiesToAdd.size + activitiesToDelete.size > 0) {
                // If there is anything to add or delete, update DB.
                repo.addAndDeleteActivityLogs(activitiesToAdd, activitiesToDelete)
            }
        }

        // Feed validated activity log data to donut.
        donutDataLive.addSource(dailyLogLive) { source ->
            val activitiesMarkedDone = source?.activities.filterDone()
            donutDataLive.value = activitiesMarkedDone.map { activity ->
                val i = coloredActivities.indexOfFirst { it.activity == activity.name }
                DonutSection(activity.name, coloredActivities[i].color, 1f)
            }
        }
    }

    fun saveDailyLog(log: DailyLog) {
        repo.addOrUpdateDailyLog(log)
    }
}