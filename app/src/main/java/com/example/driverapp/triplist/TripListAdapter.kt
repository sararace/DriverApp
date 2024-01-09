package com.example.driverapp.triplist

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.driverapp.R
import com.example.driverapp.databinding.TripListHeaderItemBinding
import com.example.driverapp.databinding.TripListRowItemBinding
import com.example.driverapp.model.Waypoint
import java.lang.StringBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TripListAdapter(private val dataSet: List<RecyclerViewItem>) : RecyclerView.Adapter<TripListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = if (viewType == VIEW_TYPE_HEADER) {
            TripListHeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            TripListRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tripItem: RecyclerViewItem = dataSet[position]
        holder.bind(tripItem)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun getItemViewType(position: Int): Int {
        return if (dataSet[position] is TripItem) VIEW_TYPE_TRIP else VIEW_TYPE_HEADER
    }

    class ViewHolder(private val itemBinding: ViewBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(tripItem: RecyclerViewItem) {
            if (itemBinding is TripListHeaderItemBinding) {
                itemBinding.date.text = tripItem.startTime?.format(DateTimeFormatter.ofPattern("E M/d")) // future improvement: localization
                itemBinding.times.text = formatTimeRange(tripItem)
                itemBinding.estimatedValue.text = "$${tripItem.estimatedEarnings.toDouble().div(100)}"
            } else if (itemBinding is TripListRowItemBinding) {
                itemBinding.times.text = formatTimeRange(tripItem)
                itemBinding.estimatedValue.text = "$${tripItem.estimatedEarnings.toDouble().div(100)}"
                itemBinding.addresses.text = formatAddresses(tripItem as TripItem)
            }
        }

        private fun formatTimeRange(tripItem: RecyclerViewItem): SpannableString {
            val start = tripItem.startTime?.format(DateTimeFormatter.ofPattern("h:mma")) ?: ""
            val end = tripItem.endTime?.format(DateTimeFormatter.ofPattern("h:mma")) ?: ""
            val spannable = SpannableString(itemView.resources.getString(R.string.time_range, start, end))
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                start.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return spannable
        }

        private fun formatAddresses(tripItem: TripItem): String {
            val addressesBuilder = StringBuilder()
            tripItem.waypoints.forEachIndexed { i, waypoint ->
                addressesBuilder.append("${i + 1}. ")
                addressesBuilder.append(waypoint.location.address)
                if (i < tripItem.waypoints.size - 1) {
                    addressesBuilder.append("\n")
                }
            }
            return addressesBuilder.toString()
        }
    }
}

open class RecyclerViewItem(
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val estimatedEarnings: Int
)
class HeaderItem(
    startTime: LocalDateTime?,
    endTime: LocalDateTime?,
    estimatedEarnings: Int
) : RecyclerViewItem(startTime, endTime, estimatedEarnings)
class TripItem(
    startTime: LocalDateTime?,
    endTime: LocalDateTime?,
    estimatedEarnings: Int,
    val waypoints: List<Waypoint>
) : RecyclerViewItem(startTime, endTime, estimatedEarnings)

const val VIEW_TYPE_HEADER = 1
const val VIEW_TYPE_TRIP = 2