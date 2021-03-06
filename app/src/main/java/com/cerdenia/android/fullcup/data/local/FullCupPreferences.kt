package com.cerdenia.android.fullcup.data.local

import android.content.Context
import android.content.SharedPreferences

object FullCupPreferences {
    private const val NAME = "full_cup_preferences"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    // SharedPreferences variables
    private const val ACTIVITIES = "activities"
    private const val USER_NAME = "user_name"
    private const val SERVER_IDS = "server_ids"
    private const val EVENT_IDS = "event_ids"
    private const val IS_NEW_USER = "is_new_user"
    private const val IS_ONBOARDED = "is_onboarded"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var activities: List<String>
        get() = preferences.getStringSet(ACTIVITIES, emptySet())?.toList()?.sorted()
            ?: emptyList()
        set(value) = preferences.edit { editor ->
            editor.putStringSet(ACTIVITIES, value.toSet())
        }

    var userName: String
        get() = preferences.getString(USER_NAME, "Peyton") ?: "Peyton"
        set(value) = preferences.edit { editor ->
            editor.putString(USER_NAME, value.trim())
        }

    var serverIds: List<String>
        get() = preferences.getStringSet(SERVER_IDS, emptySet())?.toList()
            ?: emptyList()
        set(value) = preferences.edit { editor ->
            editor.putStringSet(SERVER_IDS, value.toSet())
        }

    var eventIDs: List<String>
        get() = preferences.getStringSet(EVENT_IDS, emptySet())?.toList()
            ?: emptyList()
        set(value) = preferences.edit { editor ->
            editor.putStringSet(EVENT_IDS, value.toSet())
        }

    var isNewUser: Boolean
        get() = preferences.getBoolean(IS_NEW_USER, true)
        set(value) = preferences.edit { editor ->
            editor.putBoolean(IS_NEW_USER, value)
        }

    var isOnboarded: Boolean
        get() = preferences.getBoolean(IS_ONBOARDED, false)
        set(value) = preferences.edit { editor ->
            editor.putBoolean(IS_ONBOARDED, value)
        }
}