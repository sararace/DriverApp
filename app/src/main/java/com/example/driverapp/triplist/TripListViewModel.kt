package com.example.driverapp.triplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driverapp.model.TripData
import com.example.driverapp.model.Waypoint
import com.example.driverapp.repository.TripDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TripListViewModel(
    repository: TripDataRepository,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val tripGroups = MutableStateFlow<List<TripGroup>>(listOf())

    init {
        viewModelScope.launch(ioDispatcher) {
            repository.fetchTripData()
            repository.tripData.collect {
                tripGroups.emit(getTripGroups(it))
            }
        }
    }

    private fun getTripGroups(tripData: TripData?) : List<TripGroup> {
        tripData ?: return listOf()
        val tripGroupList = mutableListOf<TripGroup>()
        for (trip in tripData.trips) {
            val date = formatDate(trip.plannedRoute.startsAt)
            val tripGroupItem = TripGroupItem(
                formatTime(trip.plannedRoute.startsAt),
                formatTime(trip.plannedRoute.endsAt),
                trip.estimatedEarnings,
                trip.passengers.size,
                trip.passengers.count { it.boosterSeat },
                formatAddresses(trip.waypoints),
                trip.uuid
            )
            if (tripGroupList.any { it.date == date }) {
                // add to existing group list
                val tripGroup = tripGroupList.last() // this assumes trips are sorted by date
                tripGroup.endTime = formatTime(trip.plannedRoute.endsAt) // this assumes trips are sorted by time
                tripGroup.estimatedEarnings = tripGroup.estimatedEarnings + trip.estimatedEarnings
                tripGroup.tripList.add(tripGroupItem)
            } else {
                // create new group list
                val tripGroup = TripGroup(
                    date,
                    formatTime(trip.plannedRoute.startsAt),
                    formatTime(trip.plannedRoute.endsAt),
                    trip.estimatedEarnings,
                    mutableListOf(tripGroupItem)
                )
                tripGroupList.add(tripGroup)
            }
        }
        return tripGroupList
    }

    // future improvement: localization
    private fun formatDate(localDateTime: LocalDateTime?): String {
        return localDateTime?.format(DateTimeFormatter.ofPattern("E M/d")) ?: ""
    }

    private fun formatTime(localDateTime: LocalDateTime?): String {
        return localDateTime?.format(DateTimeFormatter.ofPattern("h:mma")) ?: ""
    }

    private fun formatAddresses(waypoints: List<Waypoint>): String {
        val addressesBuilder = StringBuilder()
        waypoints.forEachIndexed { i, waypoint ->
            addressesBuilder.append("${i + 1}. ")
            addressesBuilder.append(waypoint.location.streetAddress)
            addressesBuilder.append(", ")
            addressesBuilder.append(waypoint.location.city)
            addressesBuilder.append(" ")
            addressesBuilder.append((waypoint.location.zipcode))
            if (i < waypoints.size - 1) {
                addressesBuilder.append("\n")
            }
        }
        return addressesBuilder.toString()
    }

}

data class TripGroup(
    val date: String,
    val startTime: String,
    var endTime: String,
    var estimatedEarnings: Int,
    val tripList: MutableList<TripGroupItem>
)

data class TripGroupItem(
    val startTime: String,
    val endTime: String,
    val estimatedEarnings: Int,
    val riders: Int,
    val boosters: Int,
    val addresses: String,
    val tripId: String
)