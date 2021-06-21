package com.cerdenia.android.fullcup.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cerdenia.android.fullcup.data.FullCupRepository
import com.cerdenia.android.fullcup.data.model.Reminder

class SetRemindersViewModel(
    private val repo: FullCupRepository = FullCupRepository.getInstance()
) : ViewModel() {

    val remindersLive = repo.getReminders()

    fun updateReminder(reminder: Reminder) {
        repo.updateReminder(reminder)
    }

    fun confirmReminders() {
        remindersLive.value?.let { reminders ->
            repo.syncReminders(reminders)
        }
    }

    fun getAvailableTimes(): Array<String> {
        // Dummy data for now.
        val availableTimes = mutableListOf<String>()
        for (num in 0..23) {
            val hour = if (num < 10) "0$num" else num.toString()
            availableTimes.add("${hour}:00")
            availableTimes.add("${hour}:30")
        }

        return availableTimes.toTypedArray()
    }
}