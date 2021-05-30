package com.example.weather.detail

enum class TimeStage { Now, Normal, Sunset, Sunrise }

data class HourlyWeatherInformation(
    val temperature: Int?,
    val icon: String,
    val chanceOfRain: Double?,
    val chanceOfSnow: Double?,
    val datetime: Long,
    val type: TimeStage
)
