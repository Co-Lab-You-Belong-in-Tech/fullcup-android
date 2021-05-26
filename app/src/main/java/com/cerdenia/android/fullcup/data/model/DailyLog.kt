package com.cerdenia.android.fullcup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class DailyLog(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val date: Date = Date(),
    val categories: MutableList<String>,
    val completion: MutableList<Boolean> = mutableListOf(),
    var summary: String = ""
) : Serializable {
    init {
        categories.forEach { _ -> completion.add(false) }
    }

    fun getItem(category: String): Pair<String, Boolean> {
        val i = categories.indexOf(category)
        return Pair(categories[i], completion[i])
    }

    fun deleteItem(category: String) {
        val i = categories.indexOf(category)
        categories.removeAt(i)
        completion.removeAt(i)
    }

    fun markAsDone(category: String, isDone: Boolean = true) {
        val i = categories.indexOf(category)
        completion[i] = isDone
    }
}