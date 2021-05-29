package com.cerdenia.android.fullcup.ui.viewmodel

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.*
import app.futured.donut.DonutSection
import com.cerdenia.android.fullcup.DAILY
import com.cerdenia.android.fullcup.DAY_NAME_PATTERN
import com.cerdenia.android.fullcup.data.FullCupRepository
import com.cerdenia.android.fullcup.data.model.ActivityLog
import com.cerdenia.android.fullcup.data.model.ColoredCategory
import com.cerdenia.android.fullcup.data.model.DailyLog
import com.cerdenia.android.fullcup.ui.dialog.SetReminderFragment
import com.cerdenia.android.fullcup.DATE_PATTERN
import com.cerdenia.android.fullcup.util.DateTimeUtils
import java.text.SimpleDateFormat
import java.util.*

// Data source for HomeFragment.
class HomeViewModel : ViewModel() {
    private val repo = FullCupRepository.getInstance()
    private val dateLive = MutableLiveData<String>()
    val remindersLive = repo.getReminders()

    @SuppressLint("SimpleDateFormat")
    private val dayName = SimpleDateFormat(DAY_NAME_PATTERN).format(Date())
    private val typeOfDay = DateTimeUtils.toTypeOfDay(dayName)

    private val colors = listOf("#F9C06E", "#E1937B", "#E17EA3", "#739B00", "#766CE0", "#E6B5EA95")
        .map { hex -> Color.parseColor(hex) }.shuffled()

    val categories get() = remindersLive.value
        ?.filter { it.days == typeOfDay || it.days == DAILY }
        ?.map { it.category }
        ?: emptyList()

    val coloredCategories get() = categories.mapIndexed { i, category ->
        ColoredCategory(category, colors[i])
    }

    // Does not get exposed directly to view.
    private val dbDailyLogLive: LiveData<DailyLog?> = Transformations
        .switchMap(dateLive) { date -> repo.getLogByDate(date) }

    val dailyLogLive = MediatorLiveData<DailyLog?>()
    val donutDataLive = MediatorLiveData<List<DonutSection>>()

    fun onRemindersFetched() {
        dailyLogLive.addSource(dbDailyLogLive) { source ->
            // Verify data before exposing to view. We want stored
            // user-selected categories to match existing logs stored in DB.
            var activitiesToDelete: List<ActivityLog> = emptyList()
            val activitiesToAdd: MutableList<ActivityLog> = mutableListOf()

            var loggedCategories = source?.activities?.map { it.category } ?: emptyList()
            if (loggedCategories !== categories) {
                // First, get rid of logs for categories that a user has deselected.
                activitiesToDelete = source?.activities
                    ?.filter { !categories.contains(it.category) }
                    ?: emptyList()
                source?.activities?.removeAll(activitiesToDelete)
                loggedCategories = source?.activities?.map { it.category } ?: emptyList()
                // Then create new logs for categories that have been newly selected.

                categories.forEach { category ->
                    if (!loggedCategories.contains(category)) {
                        //source?.activities?.add(ActivityLog(category = category))
                        activitiesToAdd.add(ActivityLog(category = category))
                    }
                }
            }

            dailyLogLive.value = source
            repo.addActivityLog(*activitiesToAdd.toTypedArray())
            repo.deleteActivityLog(*activitiesToDelete.toTypedArray())
        }

        donutDataLive.addSource(dailyLogLive) { source ->
            val activitiesMarkedDone = source?.activities
                ?.filter { it.isDone }
                ?: emptyList()
            donutDataLive.value = activitiesMarkedDone.map { activity ->
                val i = coloredCategories.indexOfFirst { it.category == activity.category}
                DonutSection(activity.category, coloredCategories[i].color, 1f)
            }
        }
    }

    fun saveDailyLog(log: DailyLog) {
        repo.addOrUpdateDailyLog(log)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDailyLog() {
        val date = SimpleDateFormat(DATE_PATTERN).format(Date())
        dateLive.value = date
    }
}