package com.cerdenia.android.fullcup.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cerdenia.android.fullcup.data.FullCupRepository
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.data.model.Reminder

class SetRemindersViewModel : ViewModel() {
    private val repo = FullCupRepository.getInstance()

    private val categories = FullCupPreferences.categories?.toList()?.sorted() ?: emptyList()
    private val reminders = categories.map { Reminder(category = it) }

    private val _remindersLive = MutableLiveData(reminders)
    val remindersLive: LiveData<List<Reminder>> get() = _remindersLive

    fun updateReminder(reminder: Reminder) {
        val i: Int = reminders.indexOfFirst { it.category == reminder.category }
        reminders[i].apply {
            days = reminder.days
            time = reminder.time
        }

        _remindersLive.value = reminders
    }

    fun getFreeTimes(): Array<String> {
        // Dummy data for now
        val times = mutableListOf<String>()
        for (num in 0..23) {
            times.add("${num}:00")
            times.add("${num}:30")
        }
        return times.toTypedArray()
    }
}