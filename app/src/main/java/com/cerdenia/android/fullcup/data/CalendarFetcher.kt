package com.cerdenia.android.fullcup.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cerdenia.android.fullcup.data.api.BusyTimesResponse
import com.cerdenia.android.fullcup.data.api.CalendarListResponse
import com.cerdenia.android.fullcup.data.api.EventsResponse
import com.cerdenia.android.fullcup.data.api.WebService
import com.cerdenia.android.fullcup.data.model.Calendar
import com.cerdenia.android.fullcup.data.model.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "CalendarFetcher"
private const val BASE_URL = "http://192.168.1.15:4000/api/"

class CalendarFetcher {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val webService: WebService = retrofit.create(WebService::class.java)

    fun getCalendarList() {
        //val resultsLiveData = MutableLiveData<List<Calendar>>()
        Log.i(TAG, "Attempting to communicate with API...")
        val request = webService.fetchCalendarList()

        request.enqueue(object : Callback<CalendarListResponse> {
            override fun onResponse(
                call: Call<CalendarListResponse>,
                response: Response<CalendarListResponse>
            ) {
                response.body()?.calendars.let { calendars ->
                    calendars?.forEach {
                        val calendar = Calendar(it.id, it.summary, it.description)
                        Log.i(TAG, "Got calendar: ${calendar.id}")
                    }
                    //resultsLiveData.value = calendars
                }
            }

            override fun onFailure(call: Call<CalendarListResponse>, t: Throwable) {
                Log.d(TAG, "Failed to get data from API", t)
            }
        })

        //return resultsLiveData
    }
    
    fun getEvents(): MutableLiveData<List<Event>> {
        Log.i(TAG, "Attempting to communicate with API...")
        val eventsLive = MutableLiveData<List<Event>>()
        val request = webService.fetchEvents()

        request.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                Log.i(TAG, "Succesfully connected to API")
                val events = response.body()?.events?.map { event ->
                    Event(event.summary, event.start, event.end)
                }

                events?.let { eventsLive.value = it }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                Log.d(TAG, "Failed to get data from API", t)
            }
        })

        return eventsLive
    }

    fun getBusyTimes() {
        Log.i(TAG, "Attempting to communicate with API...")
        val request = webService.fetchBusyTimes()

        request.enqueue(object : Callback<BusyTimesResponse> {
            override fun onResponse(
                call: Call<BusyTimesResponse>,
                response: Response<BusyTimesResponse>
            ) {
                Log.i(TAG, "Successfully called API")
                val busyTimes = response.body()?.busyTimes
                busyTimes?.forEach { busyTime ->
                    Log.i(TAG, "Got busy time: ${busyTime.start} to ${busyTime.end}")
                }
            }

            override fun onFailure(call: Call<BusyTimesResponse>, t: Throwable) {
                Log.d(TAG, "Failed to get data from API")
            }
        })
    }
}