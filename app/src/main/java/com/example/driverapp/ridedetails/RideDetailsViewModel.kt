package com.example.driverapp.ridedetails

import androidx.lifecycle.ViewModel
import com.example.driverapp.repository.TripDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
                trip.estimatedEarnings
            )
        }?.first()
    }

    private fun formatDate(localDateTime: LocalDateTime?): String {
        return localDateTime?.format(DateTimeFormatter.ofPattern("E M/d")) ?: ""
    }

    private fun formatTime(localDateTime: LocalDateTime?): String {
        return localDateTime?.format(DateTimeFormatter.ofPattern("h:mma")) ?: ""
    }
}

data class RideDetailsUiState(
    val date: String,
    val startTime: String,
    var endTime: String,
    var estimatedEarnings: Int
)