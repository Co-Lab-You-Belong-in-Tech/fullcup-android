package com.cerdenia.android.fullcup.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WebService {
    @GET("calendar/get-calendar-list")
    fun fetchCalendarList(): Call<CalendarListResponse>

    @GET("calendar/get-busy-times")
    fun fetchBusyTimes(): Call<BusyTimesResponse>

    @POST("events/sync-events")
    fun syncEvents(@Body body: SyncEventsBody): Call<SyncEventsResponse>
}