package com.cerdenia.android.fullcup.data.api

import com.cerdenia.android.fullcup.data.model.AddEventResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WebService {
    @GET("calendar/get-calendar-list")
    fun fetchCalendarList(): Call<CalendarListResponse>

    @GET("calendar/get-events")
    fun fetchEvents(): Call<EventsResponse>

    @GET("calendar/get-busy-times")
    fun fetchBusyTimes(): Call<BusyTimesResponse>

    @POST("calendar/add-event")
    fun addEventToPrimaryCalendar(
        @Body addEventResponse: AddEventResponse
    ): Call<AddEventResponse>
}