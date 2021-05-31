package com.cerdenia.android.fullcup.data.api

import com.cerdenia.android.fullcup.data.api.SyncRemindersBody
import com.cerdenia.android.fullcup.data.api.SyncRemindersResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WebService {
    @GET("calendar/get-all-busy-times")
    fun getBusyTimes(): Call<BusyTimesResponse>

    @POST("events/sync-events")
    fun syncEvents(@Body body: SyncRemindersBody): Call<SyncRemindersResponse>
}