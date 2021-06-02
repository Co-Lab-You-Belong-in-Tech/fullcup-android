package com.cerdenia.android.fullcup.util.ext

import com.cerdenia.android.fullcup.data.model.ActivityLog

fun List<ActivityLog>?.filterDone(): List<ActivityLog> {
    return this?.filter { activity -> activity.isDone } ?: emptyList()
}

fun List<ActivityLog>?.names(): List<String> {
    return this?.map { activity -> activity.name} ?: emptyList()
}

fun List<ActivityLog>?.notIn(activities: Set<String>): List<ActivityLog> {
    return this
        ?.filter { activity -> !activities.contains(activity.name) }
        ?: emptyList()
}