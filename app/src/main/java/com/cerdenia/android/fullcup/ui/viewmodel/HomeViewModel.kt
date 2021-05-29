package com.cerdenia.android.fullcup.ui.viewmodel

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.*
import app.futured.donut.DonutSection
import com.cerdenia.android.fullcup.data.FullCupRepository
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.data.model.ActivityLog
import com.cerdenia.android.fullcup.data.model.ColoredCategory
import com.cerdenia.android.fullcup.data.model.DailyLog
import com.cerdenia.android.fullcup.util.DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {
    private val repo = FullCupRepository.getInstance()
    private val dateLive = MutableLiveData<String>()

    // time for loved ones

    private val colors = listOf("#F9C06E", "#E1937B", "#E17EA3", "#739B00", "#766CE0", "#E6B5EA95")
        .shuffled().map { colorString -> Color.parseColor(colorString) }

    val categories get() = FullCupPreferences.categories
    val coloredCategories = categories.mapIndexed { i, category ->
        ColoredCategory(category, colors[i])
    }

    // Does not get exposed directly to view.
    private val dbDailyLogLive: LiveData<DailyLog?> = Transformations
        .switchMap(dateLive) { date -> repo.getLogByDate(date) }

    val dailyLogLive = MediatorLiveData<DailyLog?>()
    val donutDataLive = MediatorLiveData<List<DonutSection>>()

    init {
        dailyLogLive.addSource(dbDailyLogLive) { source ->
            // Verify data before exposing to view. We want stored
            // user-selected categories to match existing logs stored in DB.
            lateinit var activitiesToDelete: List<ActivityLog>
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
                        source?.activities?.add(ActivityLog(category = category))
                    }
                }
            }

            dailyLogLive.value = source
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