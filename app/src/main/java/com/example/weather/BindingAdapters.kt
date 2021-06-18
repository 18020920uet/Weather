package com.example.weather

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.weather.network.responses.RelatedNameLocation
import com.example.weather.setting.PressureUnit
import com.example.weather.setting.SpeedUnit
import com.example.weather.setting.TemperatureUnit
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter(value = ["temperature", "temperatureUnit"], requireAll = true)
fun TextView.setTemperatureFormatted(temperature: Double, unit: TemperatureUnit) = temperature.let {
    text = getTemperatureText(convertTemperature(temperature, unit), unit)
}

@BindingAdapter("time")
fun TextView.setFormattedTime(timestamp: Long) = timestamp.let {
    text = SimpleDateFormat("HH:MM", Locale.ENGLISH).format(Date(timestamp * 1000))
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["chanceOfRain", "chanceOfSnow"], requireAll = true)
fun TextView.setProbabilityOfPrecipitation(chanceOfRain: Int?, chanceOfSnow: Int?) {
    text = if (chanceOfSnow != null) {
        "$chanceOfSnow%"
    } else {
        "$chanceOfRain%"
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["windSpeed", "windDegrees", "speedUnit"], requireAll = true)
fun TextView.setFormattedWind(windSpeed: Double, windDegrees: Double, speedUnit: SpeedUnit) {
    text = when {
        windDegrees < 11.25 -> "N"
        windDegrees >= 11.25 && windDegrees < 33.75 -> "NNE"
        windDegrees >= 33.75 && windDegrees < 56.25 -> "NE"
        windDegrees >= 56.25 && windDegrees < 78.75 -> "ENE"
        windDegrees >= 78.75 && windDegrees < 101.25 -> "E"
        windDegrees >= 101.25 && windDegrees < 123.75 -> "ESE"
        windDegrees >= 123.75 && windDegrees < 146.25 -> "SE"
        windDegrees >= 146.25 && windDegrees < 168.75 -> "SSE"
        windDegrees >= 168.75 && windDegrees < 191.25 -> "S"
        windDegrees >= 191.25 && windDegrees < 213.75 -> "SSW"
        windDegrees >= 213.75 && windDegrees < 236.25 -> "SW"
        windDegrees >= 236.25 && windDegrees < 258.75 -> "WSW"
        windDegrees >= 258.75 && windDegrees < 281.25 -> "W"
        windDegrees >= 281.25 && windDegrees < 303.75 -> "WNW"
        windDegrees >= 303.75 && windDegrees < 326.25 -> "NW"
        windDegrees >= 326.25 && windDegrees < 348.75 -> "NNW"
        else -> "N"
    } + " " + getSpeedText(convertSpeed(windSpeed, speedUnit), speedUnit)
}


@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["pressure", "pressureUnit"], requireAll = true)
fun TextView.setFormattedPressure(pressure: Double, unit: PressureUnit) {
    text = getPressureText(convertPressure(pressure, unit), unit)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("relatedNameLocation")
fun TextView.setRelatedNameLocation(relatedNameLocation: RelatedNameLocation) {
    text = if (relatedNameLocation.state != null) {
        "${relatedNameLocation.state}, ${relatedNameLocation.country}"
    } else {
        relatedNameLocation.country
    }
}
