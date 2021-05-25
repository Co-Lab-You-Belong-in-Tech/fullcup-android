package com.cerdenia.android.fullcup.data.local

import android.content.Context
import android.content.SharedPreferences

object FullCupPreferences {
    private const val NAME = "full_cup_preferences"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    // SharedPreferences variables
    private const val KEY_CATEGORIES = "key_categories"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var categories: Set<String>?
        get() = preferences.getStringSet(KEY_CATEGORIES, emptySet())
        set(value) = preferences.edit { editor ->
            editor.putStringSet(KEY_CATEGORIES, value)
        }
}