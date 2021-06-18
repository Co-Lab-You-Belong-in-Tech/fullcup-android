package com.cerdenia.android.fullcup.util

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import com.cerdenia.android.fullcup.DAILY
import com.cerdenia.android.fullcup.WEEKDAY
import com.cerdenia.android.fullcup.WEEKEND
import com.cerdenia.android.fullcup.data.model.Reminder
import java.net.URI
import java.util.*

class CalendarWriter(private val context: Context) {
    fun addFullCupCalendar(): Long? {
        val values = ContentValues().apply {
            put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Full Cup")
        }
        val uri: Uri? = context.contentResolver
            .insert(CalendarContract.Calendars.CONTENT_URI, values)

        Log.d(TAG, "Created $uri")
        return uri?.lastPathSegment?.toLong()
    }

    fun deleteEvents(eventIDs: List<String>) {
        eventIDs.forEach { eventID ->
            val deleteUri: Uri = ContentUris
                .withAppendedId(CalendarContract.Events.CONTENT_URI, eventID.toLong())
            val rows: Int = context.contentResolver
                .delete(deleteUri, null, null)
            Log.i(TAG, "Rows deleted: $rows")
        }
    }

    fun writeToCalendar(reminder: Reminder): String? {
        reminder.setStartDateTime()
        val dt = DateTimeUtils.breakdown(reminder.startDateTime!!)

        val calID: Long = 1 // primary calendar
        val startMillis: Long = Calendar.getInstance().run {
            set(dt.year, dt.month, dt.day, dt.hour, dt.minute)
            timeInMillis
        }
        val endMillis: Long = Calendar.getInstance().run {
            val minute = dt.minute + reminder.durationInMins
            set(dt.year, dt.month, dt.day, dt.hour, minute)
            timeInMillis
        }

        val rrule = when (reminder.recurrence) {
            // Everything defaults to a week.
            DAILY -> "FREQ=DAILY;INTERVAL=1;COUNT=7"
            WEEKDAY -> "FREQ=WEEKLY;BYDAY=SA,SU;INTERVAL=1;COUNT=2"
            WEEKEND -> "FREQ=WEEKLY;BYDAY=MO,TU,WE,TH,FR;INTERVAL=1;COUNT=5"
            else -> ""
        }

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.TITLE, reminder.name)
            put(CalendarContract.Events.DESCRIPTION, "A reminder from Full Cup.")
            put(CalendarContract.Events.CALENDAR_ID, calID)
            put(CalendarContract.Events.EVENT_TIMEZONE, reminder.timeZone)
            put(CalendarContract.Events.RRULE, rrule)
        }

        val uri: Uri? = context.contentResolver
            .insert(CalendarContract.Events.CONTENT_URI, values)

        // get the event ID that is the last element in the Uri
        val eventID: String? = uri?.lastPathSegment
        Log.d(TAG, "Event saved: $uri")
        return eventID
    }

    companion object {
        const val TAG = "CalendarWriter"
    }
}