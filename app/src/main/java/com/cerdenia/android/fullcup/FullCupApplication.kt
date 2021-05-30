package com.cerdenia.android.fullcup

import android.app.Application
import com.cerdenia.android.fullcup.data.FullCupRepository
import com.cerdenia.android.fullcup.data.api.WebService
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.data.local.db.FullCupDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FullCupApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize all data sources.
        FullCupPreferences.init(this)
        val database = FullCupDatabase.build(this)
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val webService = retrofit.create(WebService::class.java)
        FullCupRepository.init(database, webService)
    }
}