package com.example.driverapp.repository

import com.example.driverapp.model.TripData
import com.example.driverapp.network.DriverAppApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface TripRepository {
    val tripData: StateFlow<TripData?>
    suspend fun fetchTripData()
}

class TripRepositoryImpl(private val service: DriverAppApi) : TripRepository {

    override val tripData = MutableStateFlow<TripData?>(null)
    override suspend fun fetchTripData() {
        val response = service.getTripData()
        if (response.isSuccessful) {
            tripData.emit(response.body())
        }
    }
}