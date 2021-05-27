package com.cerdenia.android.fullcup.ui.viewmodel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import app.futured.donut.DonutSection
import com.cerdenia.android.fullcup.data.FullCupRepository
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.data.model.ColoredCategory
import com.cerdenia.android.fullcup.data.model.DailyLog
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {
    private val repo = FullCupRepository.getInstance()
    private val dateLive = MutableLiveData<String>()

    val dailyLogLive: LiveData<DailyLog?> = Transformations
        .switchMap(dateLive) { date -> repo.getLogByDate(date) }

    // Temporary colors
    val colors = listOf(Color.RED, Color.YELLOW, Color.BLUE,
        Color.GREEN, Color.CYAN, Color.MAGENTA)

    val categories get() = FullCupPreferences.categories?.toList()?.sorted() ?: emptyList()
    val donutCap = categories.size.toFloat()
    val donutSections = categories.mapIndexed { i, category ->
        DonutSection(
            name = category,
            amount = 1f,
            color = colors[i]
        )
    }

    fun saveDailyLog(log: DailyLog) {
        repo.addOrUpdateDailyLog(log)
    }

    fun getDailyLog() {
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
        dateLive.value = date
    }

    val coloredCategories = categories.mapIndexed { i, category ->
        ColoredCategory(category, colors[i])
    }
}