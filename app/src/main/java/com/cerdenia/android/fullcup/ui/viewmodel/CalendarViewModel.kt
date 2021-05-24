package com.cerdenia.android.fullcup.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cerdenia.android.fullcup.data.CalendarFetcher

class CalendarViewModel: ViewModel() {

    private val repo = CalendarFetcher()

    //private val _eventsLive: MutableLiveData<List<Event>> = repo.getEvents()
    //val eventsLive: LiveData<List<Event>> get() = _eventsLive

    fun getEvents() {
        // Do nothing
    }

    fun getBusyTimes() {
        repo.getBusyTimes()
    }
}