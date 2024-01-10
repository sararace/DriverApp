package com.example.driverapp.triplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
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
            viewModel.tripGroups.collect { groups ->
                val tripItems = mutableListOf<RecyclerViewItem>()
                for (group in groups) {
                    tripItems.add(
                        HeaderItem(
                            group.date,
                            group.startTime,
                            group.endTime,
                            group.estimatedEarnings
                        )
                    )
                    for (groupItem in group.tripList) {
                        tripItems.add(
                            TripItem(
                                groupItem.startTime,
                                groupItem.endTime,
                                groupItem.estimatedEarnings,
                                groupItem.riders,
                                groupItem.boosters,
                                groupItem.addresses,
                                groupItem.tripId,
                                ::navigateToRideDetails
                            )
                        )
                    }
                }
                val tripListAdapter = TripListAdapter(tripItems)

                val recyclerView: RecyclerView = binding!!.tripList
                recyclerView.adapter = tripListAdapter
                val layoutManager = LinearLayoutManager(requireContext())
                recyclerView.layoutManager = layoutManager
                // future improvement: hardcode in the dividers to solve UI bug where dividers
                // are in between card views within the same day
                val dividerItemDecoration = DividerItemDecoration(
                    recyclerView.context,
                    layoutManager.orientation
                )
                recyclerView.addItemDecoration(dividerItemDecoration)
            }
        }
    }

    private fun navigateToRideDetails(tripId: String) {
        val action = TripListFragmentDirections.actionTripListToRideDetails(tripId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}