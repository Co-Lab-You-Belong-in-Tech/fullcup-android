package com.cerdenia.android.fullcup.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cerdenia.android.fullcup.data.model.Reminder

@Database(entities = [Reminder::class], version = 1, exportSchema = false)
@TypeConverters(FullCupTypeConverters::class)
abstract class FullCupDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun logDao(): LogDao

    companion object {
        private const val NAME = "fullcup_database"

        fun build(context: Context): FullCupDatabase {
            return Room
                .databaseBuilder(context.applicationContext, FullCupDatabase::class.java, NAME)
                .build()
        }
    }
}