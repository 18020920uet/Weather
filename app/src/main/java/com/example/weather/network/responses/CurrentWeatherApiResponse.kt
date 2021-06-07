package com.example.weather.network.responses

import com.squareup.moshi.Json

data class CurrentWeatherApiResponse(
    @Json(name = "dt")
    val datetime: Long,
    val id: Int,
    @Json(name = "coord")
    val coordinates: Coordinates,
    @Json(name = "timezone")
    val offSetTimezone: Long,
    @Json(name = "name")
    val city: String,
    @Json(name = "sys")
    val sys: Sys,
    @Json(name = "main")
    val main: Main,
    @Json(name = "weather")
    val weather: List<Weather>
)

data class Coordinates(
    @Json(name = "lat")
    val latitude: Double,
    @Json(name = "lon")
    val longitude: Double
)

data class Sys(
    val type: Int,
    val country: String,
    val id: Int,
    val sunrise: Long,
    val sunset: Long
)

data class Main(
    val temp: Double, // Default value: K
    @Json(name = "feels_like")
    val feelsLike: Double,
    @Json(name = "temp_min")
    val tempMin: Double,
    @Json(name = "temp_max")
    val tempMax: Double,
    val pressure: Double,
    val humidity: Double
)
