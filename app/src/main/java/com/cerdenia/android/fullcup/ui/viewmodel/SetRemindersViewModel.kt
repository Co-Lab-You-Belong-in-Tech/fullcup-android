package com.cerdenia.android.fullcup.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cerdenia.android.fullcup.data.model.Reminder

class SetRemindersViewModel : ViewModel() {
    // Dummy data only for now
    private val reminders = listOf(
        "Health & Fitness",
        "Productivity"
    ).map { Reminder(category = it) }

    private val _remindersLive = MutableLiveData(reminders)
    val remindersLive: LiveData<List<Reminder>> get() = _remindersLive

    fun updateReminder(reminder: Reminder) {
        val i: Int = reminders.indexOfFirst { it.category == reminder.category }
        reminders[i].apply {
            days = reminder.days
            time = reminder.time
        }

        if (_remindersLive.value !== reminders) {
            _remindersLive.value = reminders
        }
    }
}