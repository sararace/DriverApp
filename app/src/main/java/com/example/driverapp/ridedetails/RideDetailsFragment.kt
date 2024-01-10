package com.example.driverapp.ridedetails

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.example.driverapp.R
import com.example.driverapp.databinding.FragmentRideDetailsBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RideDetailsFragment : Fragment() {
    private val viewModel: RideDetailsViewModel by viewModel()

    private var binding: FragmentRideDetailsBinding? = null

    val args: RideDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRideDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.toolbar?.setupWithNavController(findNavController())
        viewModel.tripIdFlow.value = args.tripId

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.rideDetailsUiStateFlow.collect { uiState ->
                binding?.date?.text = uiState?.date
                binding?.times?.text = formatTimeRange(uiState?.startTime, uiState?.endTime)
                binding?.estimatedValue?.text = "$${uiState?.estimatedEarnings?.toDouble()?.div(100)}"
            }
        }
    }

    private fun formatTimeRange(start: String?, end: String?): SpannableString {
        val spannable = SpannableString(resources.getString(R.string.time_range, start, end))
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            start?.length ?: 0,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }
}