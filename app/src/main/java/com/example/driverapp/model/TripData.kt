package com.example.driverapp.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
data class TripData(
    val trips: List<Trip>
)

data class Trip(
    @SerializedName("estimated_earnings")
    val estimatedEarnings: Int,
    @SerializedName("in_series")
    val inSeries: Boolean,
    val passengers: List<Passenger>,
    val uuid: String,
    @SerializedName("planned_route")
    val plannedRoute: PlannedRoute,
    @SerializedName("waypoints")
    val waypoints: List<Waypoint>
)

data class Passenger(
    @SerializedName("booster_seat")
    val boosterSeat: Boolean,
    val uuid: String
)

data class PlannedRoute(
    val id: Long,
    @SerializedName("total_time")
    val totalTime: Double,
    @SerializedName("total_distance")
    val totalDistance: Long,
    @SerializedName("starts_at")
    val startsAt: LocalDateTime?,
    @SerializedName("ends_at")
    val endsAt: LocalDateTime?
)

data class Waypoint(
    val id: Long,
    val location: Location
)

data class Location(
    val id: Long,
    val address: String,
    val lat: Double,
    val long: Double,
    @SerializedName("street_address")
    val streetAddress: String,
    @SerializedName("street_name")
    val streetName: String,
    @SerializedName("street_number")
    val streetNumber: String,
    val city: String,
    val zipcode: String,
    val state: String
)