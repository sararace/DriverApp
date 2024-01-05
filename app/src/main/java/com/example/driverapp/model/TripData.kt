package com.example.driverapp.model

import java.time.LocalDateTime
data class TripData(
    val trips: List<Trip>
)

data class Trip(
    val estimatedEarnings: Int,
    val inSeries: Boolean,
    val passengers: List<Passenger>,
    val uuid: String,
    val plannedRoute: PlannedRoute,
    val wayPoints: List<Waypoint>
)

data class Passenger(
    val boosterSeat: Boolean,
    val uuid: String
)

data class PlannedRoute(
    val id: Long,
    val totalTime: Double,
    val totalDistance: Long,
    val startsAt: LocalDateTime,
    val endsAt: LocalDateTime
)

data class Waypoint(
    val id: Long,
    val location: Location
)

data class Location(
    val id: Long,
    val address: String,
    val lat: Double,
    val long: Double
)