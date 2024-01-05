package com.example.driverapp.model

data class TripData(
    val trips: List<Trip>
)

data class Trip(
    val carpool: Boolean
)