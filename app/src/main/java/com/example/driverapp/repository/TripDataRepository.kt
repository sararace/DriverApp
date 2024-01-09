package com.example.driverapp.repository

import com.example.driverapp.model.TripData
import com.example.driverapp.network.DriverAppApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface TripDataRepository {
    val tripData: StateFlow<TripData?>

    suspend fun fetchTripData()
}

class TripDataRepositoryImpl(private val api: DriverAppApi) : TripDataRepository {

    override val tripData = MutableStateFlow<TripData?>(null)
    override suspend fun fetchTripData() {
        val response = api.getTripData()
        if (response.isSuccessful) {
            tripData.emit(response.body())
        }
    }
}