package com.example.weather

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["temperature", "temperatureUnit"], requireAll = true)
fun TextView.setTemperatureFormatted(temperature: Int, temperatureUnit: String) = temperature.let {
    text = getTemperatureText(temperature, temperatureUnit)
}