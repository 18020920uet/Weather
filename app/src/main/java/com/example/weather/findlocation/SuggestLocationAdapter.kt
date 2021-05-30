package com.example.weather.findlocation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.database.entities.SuggestLocation
import com.example.weather.databinding.SuggestLocationBinding

class SuggestLocationAdapter(private val suggestLocationListener: SuggestLocationListener) :
    ListAdapter<SuggestLocation, SuggestLocationAdapter.ViewHolder>(
        SuggestLocationAdapterDiffCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, suggestLocationListener)
    }

    class ViewHolder private constructor(val binding: SuggestLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SuggestLocation, suggestLocationListener: SuggestLocationListener) {
            binding.suggestLocation = item
            binding.clickListener = suggestLocationListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SuggestLocationBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SuggestLocationAdapterDiffCallBack : DiffUtil.ItemCallback<SuggestLocation>() {
    override fun areItemsTheSame(oldItem: SuggestLocation, newItem: SuggestLocation): Boolean {
        return oldItem.locationId == newItem.locationId
    }

    override fun areContentsTheSame(oldItem: SuggestLocation, newItem: SuggestLocation): Boolean {
        return oldItem == newItem
    }
}

class SuggestLocationListener(val clickListener: (suggestLocation: SuggestLocation) -> Unit) {
    fun onClick(suggestLocation: SuggestLocation) = clickListener(suggestLocation)
}