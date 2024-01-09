package com.example.driverapp.tripdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.driverapp.databinding.FragmentTripDetailBinding
import com.example.driverapp.databinding.FragmentTripListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TripDetailFragment : Fragment() {
    private val viewModel: TripDetailViewModel by viewModel()

    private var binding: FragmentTripDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}