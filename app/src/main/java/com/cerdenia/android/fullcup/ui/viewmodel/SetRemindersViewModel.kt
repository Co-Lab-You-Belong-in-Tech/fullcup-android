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