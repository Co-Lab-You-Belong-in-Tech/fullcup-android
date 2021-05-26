package com.cerdenia.android.fullcup

import android.app.Application
import com.cerdenia.android.fullcup.data.FullCupRepository
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.data.local.db.FullCupDatabase

class FullCupApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FullCupPreferences.init(this)
        val db = FullCupDatabase.build(this)
        FullCupRepository.init(this, db)
    }
}