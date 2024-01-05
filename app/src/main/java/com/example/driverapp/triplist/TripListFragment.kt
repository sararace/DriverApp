package com.example.driverapp.triplist

import androidx.fragment.app.Fragment
import com.example.driverapp.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class TripListFragment : Fragment(R.layout.fragment_trip_list) {
    private val viewModel: TripListViewModel by viewModel()

}