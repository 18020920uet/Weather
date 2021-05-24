package com.example.weather.detail

data class DailyWeatherInformation(
    val datetime: Long,
    val chanceOfRain: Double?,
    val chanceOfSnow: Double?,
    val icon: String,
    val temperatureMax: Int,
    val temperatureMin: Int,
)