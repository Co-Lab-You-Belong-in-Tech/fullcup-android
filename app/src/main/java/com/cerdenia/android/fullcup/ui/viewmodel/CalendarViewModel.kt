package com.cerdenia.android.fullcup.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cerdenia.android.fullcup.data.FullCupRepository

class CalendarViewModel(
    private val repo: FullCupRepository = FullCupRepository.getInstance()
) : ViewModel() {

    val earliestLogDateLive = repo.getEarliestLogDate()
    private val dateStringLive = MutableLiveData<String>()

    val dailyLogLive = Transformations.switchMap(dateStringLive) { dateString ->
        repo.getLogsByDate(dateString)
    }

    fun getDailyLog(dateString: String) {
        dateStringLive.value = dateString
    }
}