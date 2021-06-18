package com.cerdenia.android.fullcup.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.cerdenia.android.fullcup.data.FullCupRepository
import com.cerdenia.android.fullcup.data.model.Reminder
import com.cerdenia.android.fullcup.util.DateTimeUtils

class SetRemindersViewModel : ViewModel() {
    private val repo = FullCupRepository.getInstance()
    val remindersLive = repo.getReminders()

    fun updateReminder(reminder: Reminder) {
        repo.updateReminder(reminder)
    }

    fun confirmReminders() {
        remindersLive.value?.let { reminders ->
            //repo.syncRemindersWithCalendar(reminders)
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

    /*
    fun updateAvailableTimes(reminders: List<Reminder>) {
        reminders.forEach { reminder ->
            reminder.time?.let { startTime ->
                var endTimeHour = startTime.substringBefore(":").toInt()
                var endTimeMinutes = startTime.substringAfter(":").toInt()
                    .plus(reminder.durationInMins)

                while (endTimeMinutes >= 60) {
                    endTimeMinutes -= 60
                    endTimeHour += 1
                }

                val endTime = DateTimeUtils.to24HourFormat(endTimeHour, endTimeMinutes)
                availableTimes.removeAll(listOf(startTime, endTime))
            }
        }
    }
     */
}