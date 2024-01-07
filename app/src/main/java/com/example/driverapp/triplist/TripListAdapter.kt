package com.example.driverapp.triplist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            itemBinding.date.text = tripItem.plannedRoute.startsAt?.format(DateTimeFormatter.ISO_DATE)
            itemBinding.times.text = tripItem.plannedRoute.startsAt?.format(DateTimeFormatter.ISO_TIME)
            itemBinding.estimatedValue.text = "$${tripItem.estimatedEarnings.toDouble().div(100)}"
        }
    }
}