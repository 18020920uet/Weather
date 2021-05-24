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
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class DailyWeatherInformationAdapter :
    ListAdapter<DailyWeatherInformation, DailyWeatherInformationAdapter.ViewHolder>(
        DailyWeatherInformationDiffCallBack()
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
        private val weekDay = itemView.findViewById<TextView>(R.id.daily_week_day)
        private val weatherIcon = itemView.findViewById<ImageView>(R.id.daily_weather_icon)
        private val pop = itemView.findViewById<TextView>(R.id.daily_pop)
        private val temperatureMax = itemView.findViewById<TextView>(R.id.daily_temperature_max)
        private val temperatureMin = itemView.findViewById<TextView>(R.id.daily_temperature_min)


        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(item: DailyWeatherInformation) {
            val time = Date(item.datetime * 1000)
            val currentTime = Date(System.currentTimeMillis())
            val date = SimpleDateFormat("EEEE", Locale.ENGLISH).format(time)


            if (currentTime.date == time.date) {
                weekDay.text = "Today"
            } else {
                weekDay.text = date
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

            when {
                item.chanceOfRain != null -> {
                    val value = (item.chanceOfRain * 100).toInt()
                    pop.text = "$value%"
                }
                item.chanceOfSnow != null -> {
                    val value = (item.chanceOfSnow * 100).toInt()
                    pop.text = "$value%"
                }
                else -> {
                    pop.visibility = View.GONE
                }
            }

            temperatureMax.text = getTemperatureText(item.temperatureMax, unit)
            temperatureMin.text = getTemperatureText(item.temperatureMin, unit)
        }

        companion object {
            lateinit var unit: TemperatureUnit
            fun from(parent: ViewGroup, temperatureUnit: TemperatureUnit): ViewHolder {
                unit = temperatureUnit
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.daily_weather_information, parent, false)
                return ViewHolder(view)
            }
        }
    }
}

class DailyWeatherInformationDiffCallBack : DiffUtil.ItemCallback<DailyWeatherInformation>() {
    override fun areItemsTheSame(
        oldItem: DailyWeatherInformation,
        newItem: DailyWeatherInformation
    ): Boolean {
        return oldItem.datetime == newItem.datetime
    }

    override fun areContentsTheSame(
        oldItem: DailyWeatherInformation,
        newItem: DailyWeatherInformation
    ): Boolean {
        return oldItem == newItem
    }
}