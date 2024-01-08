package com.example.driverapp.triplist

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.driverapp.R
import com.example.driverapp.databinding.TripListHeaderItemBinding
import com.example.driverapp.model.Trip
import java.time.format.DateTimeFormatter

class TripListAdapter(private val dataSet: List<Trip>) : RecyclerView.Adapter<TripListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = TripListHeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tripItem: Trip = dataSet[position]
        holder.bind(tripItem)
    }

    override fun getItemCount(): Int = dataSet.size

    class ViewHolder(private val itemBinding: TripListHeaderItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(tripItem: Trip) {
            itemBinding.date.text = tripItem.plannedRoute.startsAt?.format(DateTimeFormatter.ofPattern("E M/d")) // future improvement: localization
            itemBinding.times.text = formatTimeRange(tripItem)
            itemBinding.estimatedValue.text = "$${tripItem.estimatedEarnings.toDouble().div(100)}"
        }

        private fun formatTimeRange(tripItem: Trip) : SpannableString {
            val start = tripItem.plannedRoute.startsAt?.format(DateTimeFormatter.ofPattern("h:mma")) ?: ""
            val end = tripItem.plannedRoute.endsAt?.format(DateTimeFormatter.ofPattern("h:mma")) ?: ""
            val spannable = SpannableString(itemView.resources.getString(R.string.time_range, start, end))
            spannable.setSpan(StyleSpan(Typeface.BOLD), 2, start.length + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannable
        }
    }
}