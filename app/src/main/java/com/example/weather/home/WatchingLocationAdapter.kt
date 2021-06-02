package com.example.weather.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.database.entities.Location
import com.example.weather.databinding.WatchingLocationBinding
import com.example.weather.setTemperatureFormatted
import com.example.weather.setting.TemperatureUnit
import java.text.SimpleDateFormat
import java.util.*

class WatchingLocationAdapter(
    private val watchLocationClickListener: WatchingLocationClickListener,
    private val temperatureUnit: TemperatureUnit,
) :
    ListAdapter<Location, WatchingLocationAdapter.ViewHolder>(
        WatchingLocationDiffCallback()
    ) {
    class ViewHolder private constructor(private val binding: WatchingLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(item: Location, watchingLocationClickListener: WatchingLocationClickListener) {
            binding.location = item
            binding.locationName.text = "${item.locationName}, ${item.country}"
            binding.locationTemperature.setTemperatureFormatted(
                item.temperature.toDouble(),
                temperatureUnit
            )

            val timeZone = TimeZone.getAvailableIDs((item.timeZoneOffSet * 1000).toInt())
            if (timeZone.isEmpty()) {
                val timeZoneConvert = -offSetTimeZone + item.timeZoneOffSet * 1000
                val time = System.currentTimeMillis() + timeZoneConvert
                binding.time.text = SimpleDateFormat("HH:mm:ss").format(time)
            } else {
                binding.time.timeZone = timeZone[0]
                binding.time.format24Hour = "HH:mm:ss"
            }
            binding.clickListener = watchingLocationClickListener
        }

        companion object {
            val offSetTimeZone: Int = Calendar.getInstance().timeZone.rawOffset
            var temperatureUnit: TemperatureUnit = TemperatureUnit.Celsius

            fun from(parent: ViewGroup, unit: TemperatureUnit): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = WatchingLocationBinding.inflate(inflater, parent, false)
                this.temperatureUnit = unit
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, temperatureUnit)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, watchLocationClickListener)
    }
}

class WatchingLocationDiffCallback() : DiffUtil.ItemCallback<Location>() {
    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.id == newItem.id && oldItem.temperature == newItem.temperature
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location) = oldItem == newItem
}

class WatchingLocationClickListener(val clickListener: (location: Location) -> Unit) {
    fun onClick(location: Location) = clickListener(location)
}