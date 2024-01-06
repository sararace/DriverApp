package com.example.driverapp.triplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driverapp.databinding.FragmentTripListBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TripListFragment : Fragment() {
    private val viewModel: TripListViewModel by viewModel()

    private var binding: FragmentTripListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tripData.collect { data ->
                data?.let {
                    val tripListAdapter =
                        TripListAdapter(it.trips.map { trip -> trip.plannedRoute?.totalTime.toString() }
                            .toTypedArray())

                    val recyclerView: RecyclerView = binding!!.tripList
                    recyclerView.adapter = tripListAdapter
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}