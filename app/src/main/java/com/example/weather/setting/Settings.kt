package com.example.weather.setting

data class Settings(
    var temperatureUnit: TemperatureUnit = TemperatureUnit.Celsius,
    val speedUnit: SpeedUnit = SpeedUnit.metresPerSecond, // m/s - km/h - mph
    val pressureUnit: PressureUnit = PressureUnit.hPa,
    val language: String? = "en"
)


enum class TemperatureUnit {
    Celsius,
    Kelvin,
    Fahrenheit
}

enum class PressureUnit {
    hPa,
    Pa,
}

enum class SpeedUnit {
    metresPerSecond,
    kilometersPerSHour,
    milesPerHour
}