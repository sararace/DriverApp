package com.example.driverapp.network

import com.example.driverapp.model.TripData
import retrofit2.Response
import retrofit2.http.GET

interface DriverAppApi {
    @GET("/hsd-interview-resources/mobile_coding_challenge_data.json")
    suspend fun getTripData(): Response<TripData>
}