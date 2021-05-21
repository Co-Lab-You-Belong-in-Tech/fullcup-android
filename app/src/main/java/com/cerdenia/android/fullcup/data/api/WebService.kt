package com.cerdenia.android.fullcup.data.api

import retrofit2.Call
import retrofit2.http.GET

interface WebService {

    @GET("calendar/get-calendar-list")
    fun fetchCalendarList(): Call<CalendarListResponse>

    @GET("calendar/get-events")
    fun fetchEvents(): Call<EventsResponse>

    @GET("calendar/get-busy-times")
    fun fetchBusyTimes(): Call<BusyTimesResponse>
}