package com.example.weather.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.getTemperatureText
import com.example.weather.setting.TemperatureUnit
import java.text.SimpleDateFormat
import java.util.*

class HourlyWeatherInformationAdapter() :
    ListAdapter<HourlyWeatherInformation, HourlyWeatherInformationAdapter.ViewHolder>(
        HourlyWeatherInformationDiffCallback()
    ) {

    lateinit var temperatureUnit: TemperatureUnit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, temperatureUnit)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hour: TextView = itemView.findViewById(R.id.hourly_time)
        private val weatherIcon: ImageView = itemView.findViewById(R.id.hourly_icon)
        private val temperature: TextView = itemView.findViewById(R.id.hourly_temperature)

        @SuppressLint("SimpleDateFormat")
        fun bind(item: HourlyWeatherInformation) {
            val time = Date(item.datetime * 1000)
            hour.text = SimpleDateFormat("HH:mm").format(time)

            temperature.text = when (item.type) {
                TimeStage.Normal -> item.temperature?.let { getTemperatureText(it, unit) }
                TimeStage.Sunset -> "Sunset"
                TimeStage.Sunrise -> "Sunrise"
                TimeStage.Now -> "Now"
            }

            weatherIcon.setImageResource(
                when (item.icon) {
                    "cloud" -> R.drawable.cloud
                    "thunderstorm" -> R.drawable.thunderstorm
                    "drizzle" -> R.drawable.mist
                    "rain" -> R.drawable.rain
                    "snow" -> R.drawable.snow
                    "mist" -> R.drawable.mist
                    "clear" -> R.drawable.clear
                    "sunset" -> R.drawable.sunset
                    "sunrise" -> R.drawable.sunrise
                    else -> R.drawable.clear
                }
            )
        }

        companion object {
            lateinit var unit: TemperatureUnit
            fun from(parent: ViewGroup, temperatureUnit: TemperatureUnit): ViewHolder {
                unit = temperatureUnit
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.hourly_weather_information, parent, false)
                return ViewHolder(view)
            }
        }
    }
}

class HourlyWeatherInformationDiffCallback : DiffUtil.ItemCallback<HourlyWeatherInformation>() {
    override fun areItemsTheSame(
        oldItem: HourlyWeatherInformation, newItem: HourlyWeatherInformation
    ): Boolean {
        return oldItem.datetime == newItem.datetime
    }

    override fun areContentsTheSame(
        oldItem: HourlyWeatherInformation, newItem: HourlyWeatherInformation
    ): Boolean {
        return oldItem == newItem
    }
}