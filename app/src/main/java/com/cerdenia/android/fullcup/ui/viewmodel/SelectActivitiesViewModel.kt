package com.cerdenia.android.fullcup.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cerdenia.android.fullcup.data.FullCupRepository
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.data.model.Reminder

class SelectActivitiesViewModel : ViewModel() {
    private val repo = FullCupRepository.getInstance()
    val activities get() = FullCupPreferences.activities
    val userName get() = FullCupPreferences.userName

    fun submitActivities(selected: List<String>, deselected: List<String>) {
        // Save selected categories to SharedPreferences.
        FullCupPreferences.activities = selected
        // Create new Reminder objects for newly selected categories.
        val newReminders = selected.map { activity -> Reminder(name = activity) }
        repo.updateReminderSet(newReminders, deselected)
    }

    fun submitUserName(userName: String) {
        // Save user name to SharedPreferences.
        FullCupPreferences.userName = userName
    }
}