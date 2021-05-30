package com.example.weather.findlocation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.RelatedNameLocationBinding
import com.example.weather.network.responses.RelatedNameLocation

class RelatedNameLocationAdapter(private val relatedNameLocationListener: RelatedNameLocationListener) :
    ListAdapter<RelatedNameLocation, RelatedNameLocationAdapter.ViewHolder>(
        RelatedNameLocationDiffCallback()
    ) {

    class ViewHolder private constructor(val binding: RelatedNameLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RelatedNameLocation, clickListener: RelatedNameLocationListener) {
            binding.relatedNameLocation = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RelatedNameLocationBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, relatedNameLocationListener)
    }
}

class RelatedNameLocationDiffCallback : DiffUtil.ItemCallback<RelatedNameLocation>() {
    override fun areItemsTheSame(old: RelatedNameLocation, new: RelatedNameLocation): Boolean {
        return new == old
    }

    override fun areContentsTheSame(old: RelatedNameLocation, new: RelatedNameLocation): Boolean {
        return new == old
    }
}

class RelatedNameLocationListener(val clickListener: (relatedNameLocation: RelatedNameLocation) -> Unit) {
    fun onClick(relatedNameLocation: RelatedNameLocation) = clickListener(relatedNameLocation)
}
