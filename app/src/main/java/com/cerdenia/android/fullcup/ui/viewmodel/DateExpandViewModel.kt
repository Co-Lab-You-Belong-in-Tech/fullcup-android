package com.cerdenia.android.fullcup.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cerdenia.android.fullcup.data.FullCupRepository
import com.cerdenia.android.fullcup.data.model.DailyLog

class DateExpandViewModel : ViewModel() {
    private val repo = FullCupRepository.getInstance()

    private val dateStringLive = MutableLiveData<String>()
    val dailyLoglive = Transformations.switchMap(dateStringLive) { dateString ->
        repo.getLogsByDate(dateString)
    }

    fun getDailyLog(dateString: String) {
        dateStringLive.value = dateString
    }
}