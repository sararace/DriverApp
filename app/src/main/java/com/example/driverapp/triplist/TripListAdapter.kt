package com.example.driverapp.triplist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.driverapp.databinding.TripListRowItemBinding

class TripListAdapter(private val dataSet: Array<String>) : RecyclerView.Adapter<TripListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = TripListRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tripItem: String = dataSet[position]
        holder.bind(tripItem)
    }

    override fun getItemCount(): Int = dataSet.size

    class ViewHolder(private val itemBinding: TripListRowItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(tripItem: String) {
            itemBinding.tripItemTimes.text = tripItem
        }
    }
}