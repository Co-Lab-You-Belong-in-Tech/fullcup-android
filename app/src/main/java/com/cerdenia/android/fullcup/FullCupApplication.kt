package com.cerdenia.android.fullcup

import android.app.Application
import com.cerdenia.android.fullcup.data.local.FullCupPreferences

class FullCupApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FullCupPreferences.init(this)
    }
}