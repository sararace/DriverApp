package com.example.driverapp.ridedetails

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.example.driverapp.R
import com.example.driverapp.model.Waypoint
import com.example.driverapp.repository.TripDataRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class RideDetailsViewModel(
    repository: TripDataRepository
) : ViewModel() {

    val tripIdFlow = MutableStateFlow("")

    val rideDetailsUiStateFlow = repository.tripData.combine(tripIdFlow) { tripData, tripId ->
        tripData?.trips?.filter { trip -> trip.uuid == tripId }?.map { trip ->
            RideDetailsUiState(
                formatDate(trip.plannedRoute.startsAt),
                formatTime(trip.plannedRoute.startsAt),
                formatTime(trip.plannedRoute.endsAt),
                trip.estimatedEarnings,
                trip.inSeries,
                getWaypoints(trip.waypoints),
                trip.uuid,
                metersToMiles(trip.plannedRoute.totalDistance),
                trip.plannedRoute.totalTime
            )
        }?.first()
    }

    val waypointsFlow = repository.tripData.combine(tripIdFlow) { tripData, tripId ->
        tripData?.trips?.filter { trip -> trip.uuid == tripId }?.map { trip ->
            val waypoints = mutableListOf<LatLng>()
            waypoints.add(LatLng(trip.waypoints.first().location.lat, trip.waypoints.first().location.lng))
            waypoints.add(LatLng(trip.waypoints.last().location.lat, trip.waypoints.last().location.lng))
            waypoints
        }?.first()
    }

    private fun formatDate(localDateTime: LocalDateTime?): String {
        return localDateTime?.format(DateTimeFormatter.ofPattern("E M/d")) ?: ""
    }

    private fun formatTime(localDateTime: LocalDateTime?): String {
        return localDateTime?.format(DateTimeFormatter.ofPattern("h:mma")) ?: ""
    }

    private fun getWaypoints(waypoints: List<Waypoint>): List<WaypointUiState> {
        val uiStateList = mutableListOf<WaypointUiState>()
        waypoints.forEachIndexed { index, waypoint ->
            val address = waypoint.location.streetAddress
            val city = waypoint.location.city
            val state = waypoint.location.state
            uiStateList.add(
                WaypointUiState(
                    if (index < waypoints.size - 1) WaypointType.PICKUP else WaypointType.DROP_OFF,
                    "$address, $city, $state"
                )
            )
        }
        return uiStateList
    }

    private fun metersToMiles(distanceInMeters: Long): Double {
        return (distanceInMeters * METERS_TO_MILES * 10).roundToInt() / 10.0
    }

    companion object {
        const val METERS_TO_MILES = 0.000621371
    }
}

data class RideDetailsUiState(
    val date: String,
    val startTime: String,
    val endTime: String,
    val estimatedEarnings: Int,
    val series: Boolean,
    val waypoints: List<WaypointUiState>,
    val tripId: String,
    val distance: Double,
    val totalTime: Double
)

data class WaypointUiState(
    val type: WaypointType,
    val address: String
)

enum class WaypointType(
    @StringRes
    val title: Int,
    @DrawableRes
    val icon: Int
) {
    PICKUP(R.string.pickup_title, R.drawable.anchor_icon),
    DROP_OFF(R.string.drop_off_title, R.drawable.non_anchor_icon)
}