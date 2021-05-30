package com.cerdenia.android.fullcup.data.local

import android.content.Context
import android.content.SharedPreferences

object FullCupPreferences {
    private const val NAME = "full_cup_preferences"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    // SharedPreferences variables
    private const val CATEGORIES = "categories"
    private const val USER_NAME = "user_name"
    private const val GOOGLE_EVENT_IDS = "google_event_ids"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var categories: List<String>
        get() = preferences.getStringSet(CATEGORIES, emptySet())
            ?.toList()
            ?.sorted()
            ?: emptyList()
        set(value) = preferences.edit { editor ->
            editor.putStringSet(CATEGORIES, value.toSet())
        }

    var userName: String?
        get() = preferences.getString(USER_NAME, "Peyton") ?: "Peyton"
        set(value) = preferences.edit { editor ->
            editor.putString(USER_NAME, value)
        }

    var googleEventIds: List<String>
        get() = preferences.getStringSet(GOOGLE_EVENT_IDS, emptySet())
            ?.toList()
            ?: emptyList()
        set(value) = preferences.edit { editor ->
            editor.putStringSet(GOOGLE_EVENT_IDS, value.toSet())
        }
}