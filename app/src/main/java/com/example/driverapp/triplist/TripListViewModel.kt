package com.example.driverapp.triplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driverapp.model.TripData
import com.example.driverapp.network.DriverAppApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TripListViewModel(
    driverAppService: DriverAppApi,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val tripData = MutableStateFlow<TripData?>(null)

    init {
        viewModelScope.launch(ioDispatcher) {
            val response = driverAppService.getTripData()
            if (response.isSuccessful) {
                tripData.emit(response.body())
            }
        }
    }
}