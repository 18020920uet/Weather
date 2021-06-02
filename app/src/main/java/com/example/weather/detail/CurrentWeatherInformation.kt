package com.example.weather.detail

import com.example.weather.network.responses.Weather

data class CurrentWeatherInformation(
    val locationName: String = "",
    val temperature: Double,
    val sunrise: Long,
    val sunset: Long,
    val chanceOfRain: Int?,
    val chanceOfSnow: Int?,
    val humidity: Int,
    val windSpeed: Double,
    val windDegrees: Double,
    val pressure: Double,
    val visibility: Int,
    val uvi: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val weatherStatus: Weather
)