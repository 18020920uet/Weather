package com.example.weather.network

import com.squareup.moshi.Json

data class CurrentWeatherApiResponse(
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
    val main: Main
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

data class WeatherDetailResponse(
    @Json(name = "timezone_offset")
    val offSetTimezone: Long,
    @Json(name = "lat")
    val latitude: Double,
    @Json(name = "lon")
    val longitude: Double,
    @Json(name = "current")
    val currentWeatherDetail: CurrentWeather,
    @Json(name = "hourly")
    val hourly: List<Hourly>,
    @Json(name = "daily")
    val daily: List<Daily>
)

data class CurrentWeather(
    @Json(name = "dt")
    val datetime: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    @Json(name = "feels_like")
    val feelsLike: Double,
    // Áp suất, đơn vị: hPa
    val pressure: Double,
    // Độ ẩm
    val humidity: Double,
    val uvi: Double,
    // tỉ lệ % có mây
    val clouds: Double,
    // Đơn vị: meter
    val visibility: Double,
    @Json(name = "wind_speed")  // Đơn vị: metresPerSecond
    val windSpeed: Double,
    @Json(name = "wind_deg")
    val windDegrees: Double,
    @Json(name = "weather")
    val weather: List<Weather>
)

data class Hourly(
    @Json(name = "dt")
    val datetime: Long,
    val temp: Double,
    @Json(name = "rain")
    val rain: Volume?,
    @Json(name = "snow")
    val snow: Volume?,
    // Probability of precipitation tỉ lệ có mưa/tuyết
    val pop: Double = 0.0,
    @Json(name = "weather")
    val weather: List<Weather>
)

data class Volume(
    @Json(name = "1h")
    val volume: Double,
)

data class Daily(
    @Json(name = "dt")
    val datetime: Long,
    @Json(name = "sunrise")
    val sunrise: Long,
    @Json(name = "sunset")
    val sunset: Long,
    @Json(name = "temp")
    val temperatureDetail: TemperatureDetail,
    @Json(name = "rain")
    val rain: Double?,
    val snow: Double?,
    // Probability of precipitation tỉ lệ có mưa/tuyết
    val pop: Double = 0.0,
    @Json(name = "weather")
    val weather: List<Weather>
)

data class TemperatureDetail(
    @Json(name = "morn")
    val morning: Double,
    @Json(name = "day")
    val day: Double,
    @Json(name = "eve")
    val evening: Double,
    @Json(name = "night")
    val night: Double,
    @Json(name = "min")
    val min: Double,
    @Json(name = "max")
    val max: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)