package com.cerdenia.android.fullcup.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cerdenia.android.fullcup.data.FullCupRepository
import com.cerdenia.android.fullcup.data.model.Reminder

class SetRemindersViewModel : ViewModel() {
    private val repo = FullCupRepository.getInstance()
    val remindersLive = repo.getReminders()

    fun updateReminder(reminder: Reminder) {
        repo.updateReminder(reminder)
    }

    fun confirmReminders() {
        remindersLive.value?.let { reminders ->
            repo.syncRemindersWithCalendar(reminders)
        }
    }

    fun getAvailableTimes(): Array<String> {
        // Dummy data for now
        val times = mutableListOf<String>()
        for (num in 0..23) {
            val hour = if (num < 10) "0$num" else num.toString()
            times.add("${hour}:00")
            times.add("${hour}:30")
        }
        return times.toTypedArray()
    }
}