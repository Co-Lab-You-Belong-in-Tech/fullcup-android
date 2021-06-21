package com.cerdenia.android.fullcup.util

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import com.cerdenia.android.fullcup.DAILY
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.WEEKDAY
import com.cerdenia.android.fullcup.WEEKEND
import com.cerdenia.android.fullcup.data.model.Reminder

class CalendarWriter(private val context: Context) {

    fun addEvents(reminders: List<Reminder>): List<String> {
        val eventIDs = mutableListOf<String>()

        for (reminder in reminders) {
            reminder.setStartDateTime() // Ensures that startDateTime is not null.
            val (y, mo, d, h, mi) = DateTimeUtils.breakdown(reminder.startDateTime!!)

            val startMillis = DateTimeUtils.getTimeInMillis(y, mo, d, h, mi)
            val endMinute = mi + reminder.durationInMins
            val endMillis = DateTimeUtils.getTimeInMillis(y, mo, d, h, endMinute)
            val description = context.getString(R.string.a_reminder_from_full_cup)
            val rrule = when (reminder.recurrence) {
                // Everything defaults to a week.
                WEEKDAY -> "FREQ=WEEKLY;BYDAY=MO,TU,WE,TH,FR;INTERVAL=1;COUNT=5"
                WEEKEND -> "FREQ=WEEKLY;BYDAY=SA,SU;INTERVAL=1;COUNT=2"
                DAILY -> "FREQ=DAILY;INTERVAL=1;COUNT=7"
                else -> ""
            }

            val values = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, startMillis)
                put(CalendarContract.Events.DTEND, endMillis)
                put(CalendarContract.Events.TITLE, reminder.name)
                put(CalendarContract.Events.DESCRIPTION, description)
                put(CalendarContract.Events.CALENDAR_ID, 1) // Primary calendar.
                put(CalendarContract.Events.EVENT_TIMEZONE, reminder.timeZone)
                put(CalendarContract.Events.RRULE, rrule)
            }

            val uri: Uri? = context.contentResolver
                .insert(CalendarContract.Events.CONTENT_URI, values)
            // Get the event ID that is the last element in the Uri.
            uri?.lastPathSegment?.let { eventID -> eventIDs.add(eventID) }
        }

        Log.d(TAG, "Saved events: $eventIDs")
        return eventIDs
    }

    fun deleteEvents(eventIDs: List<String>) {
        for (eventID in eventIDs) {
            val deleteUri: Uri = ContentUris
                .withAppendedId(CalendarContract.Events.CONTENT_URI, eventID.toLong())
            val rows: Int = context.contentResolver
                .delete(deleteUri, null, null)
            Log.d(TAG, "Rows deleted: $rows")
        }
    }

    companion object {

        const val TAG = "CalendarWriter"
    }
}