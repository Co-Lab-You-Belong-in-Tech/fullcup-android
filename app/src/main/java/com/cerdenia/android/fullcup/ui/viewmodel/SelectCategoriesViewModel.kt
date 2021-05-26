package com.cerdenia.android.fullcup.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cerdenia.android.fullcup.data.FullCupRepository
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.data.model.Reminder

class SelectCategoriesViewModel : ViewModel() {
    private val repo = FullCupRepository.getInstance()
    val categories: Set<String> get() = FullCupPreferences.categories ?: emptySet()

    fun submitCategories(selected: List<String>, deselected: List<String>) {
        // Save selected categories to SharedPreferences.
        FullCupPreferences.categories = selected.toSet()
        // Create new Reminder objects for newly selected categories.
        val newReminders = selected.map { Reminder(category = it )}
        repo.updateReminderSet(newReminders, deselected)
    }
}