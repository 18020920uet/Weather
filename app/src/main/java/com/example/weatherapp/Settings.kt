package com.example.weatherapp

data class Settings(
    var temperatureUnit: String? = "Celsius", // Celsius - Kelvin - Fahrenheit
    val speedUnit: String? = "m/s", // m/s - km/h - mph
    val pressureUnit: String? = "Pa", // Pa - atm -
    val language: String? = "en"// en - vn
)
