package com.cerdenia.android.fullcup.data.api

import com.cerdenia.android.fullcup.data.model.Reminder

data class SyncRemindersBody(
    val events: List<Reminder>,
    val idsForDeletion: List<String>
)