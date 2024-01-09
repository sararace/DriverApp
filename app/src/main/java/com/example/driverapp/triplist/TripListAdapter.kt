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
                itemBinding.date.text = (tripItem as HeaderItem).date
                itemBinding.times.text = formatTimeRange(tripItem.startTime, tripItem.endTime)
                itemBinding.estimatedValue.text = "$${tripItem.estimatedEarnings.toDouble().div(100)}"
            } else if (itemBinding is TripListRowItemBinding) {
                itemBinding.times.text = formatTimeRange(tripItem.startTime, tripItem.endTime)
                itemBinding.estimatedValue.text = "$${tripItem.estimatedEarnings.toDouble().div(100)}"
                itemBinding.riderBoosterCount.text = formatRidersBoosters(tripItem as TripItem)
                itemBinding.addresses.text = tripItem.addresses
                itemView.setOnClickListener { tripItem.onClick(tripItem.tripId) }
            }
        }

        private fun formatTimeRange(start: String, end: String): SpannableString {
            val spannable = SpannableString(itemView.resources.getString(R.string.time_range, start, end))
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                start.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return spannable
        }

        private fun formatRidersBoosters(tripItem: TripItem): String {
            val ridersString = itemView.resources.getQuantityString(R.plurals.riders, tripItem.riders, tripItem.riders)
            val boostersString = itemView.resources.getQuantityString(R.plurals.boosters, tripItem.boosters, tripItem.boosters)
            return if (tripItem.boosters > 0) {
                "($ridersString • $boostersString)"
            } else {
                "($ridersString)"
            }
        }
    }
}

open class RecyclerViewItem(
    val startTime: String,
    val endTime: String,
    val estimatedEarnings: Int
)
class HeaderItem(
    val date: String,
    startTime: String,
    endTime: String,
    estimatedEarnings: Int
) : RecyclerViewItem(startTime, endTime, estimatedEarnings)
class TripItem(
    startTime: String,
    endTime: String,
    estimatedEarnings: Int,
    val riders: Int,
    val boosters: Int,
    val addresses: String,
    val tripId: String,
    val onClick: (String) -> Unit
) : RecyclerViewItem(startTime, endTime, estimatedEarnings)

const val VIEW_TYPE_HEADER = 1
const val VIEW_TYPE_TRIP = 2