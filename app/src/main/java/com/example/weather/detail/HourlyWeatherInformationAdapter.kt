package com.example.weather.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.setTemperatureFormatted
import com.example.weather.setting.TemperatureUnit
import java.text.SimpleDateFormat
import java.util.*

class HourlyWeatherInformationAdapter :
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

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(item: HourlyWeatherInformation) {
            itemView.setBackgroundColor(Color.parseColor("#f2fcfe"))
            val time = Date(item.datetime * 1000)
            hour.text = SimpleDateFormat("HH:mm").format(time)

            when (item.type) {
                TimeStage.Normal -> item.temperature?.let {
                    temperature.setTemperatureFormatted(it, unit)
                }
                TimeStage.Sunset -> temperature.text = "Sunset"
                TimeStage.Sunrise -> temperature.text = "Sunrise"
                TimeStage.Now -> temperature.text = "Now"
            }

            weatherIcon.setImageResource(
                when (item.icon) {
                    "mist_day" -> R.drawable.mist_day
                    "day" -> R.drawable.day
                    "cloudy_day" -> R.drawable.cloudy_day
                    "thunderstorm" -> R.drawable.thunderstorm
                    "rain" -> R.drawable.rain
                    "snow" -> R.drawable.snow

                    "sunset" -> R.drawable.sunset
                    "sunrise" -> R.drawable.sunrise

                    "mist_night" -> R.drawable.mist_night
                    "cloudy_night" -> R.drawable.cloudy_night
                    "night" -> R.drawable.night
                    else -> R.drawable.day
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