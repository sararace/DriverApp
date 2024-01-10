package com.example.driverapp.ridedetails

import androidx.lifecycle.ViewModel
import com.example.driverapp.repository.TripDataRepository
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
                trip.uuid,
                metersToMiles(trip.plannedRoute.totalDistance),
                trip.plannedRoute.totalTime
            )
        }?.first()
    }

    private fun formatDate(localDateTime: LocalDateTime?): String {
        return localDateTime?.format(DateTimeFormatter.ofPattern("E M/d")) ?: ""
    }

    private fun formatTime(localDateTime: LocalDateTime?): String {
        return localDateTime?.format(DateTimeFormatter.ofPattern("h:mma")) ?: ""
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
    val tripId: String,
    val distance: Double,
    val totalTime: Double
)