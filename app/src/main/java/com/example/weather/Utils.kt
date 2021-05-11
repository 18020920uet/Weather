package com.example.weather

/**
 *  Convert temperature to setting's temperature unit
 *  @param  temperature - temperature value with default unit = K
 *  @param  unit - type of temperature unit: K - C - F
 *  @return result convert value
 */

fun convertTemperature(temperature: Double, unit: String): Int {
    return when (unit) {
        "Kelvin" -> temperature.toInt()
        "Celsius" -> (temperature - 273.15).toInt()
        "Fahrenheit" -> ((temperature - 273.15) * 9 / 5 + 32).toInt()
        else -> throw Exception("Undefined unit's type value")
    }
}

/**
 *  Get string value to use in fragment
 *  @param  temperature - temperature value
 *  @param  unit - type of temperature unit: C - F
 *  @return result convert value
 */

fun getTemperatureText(temperature: Int, unit: String): String {
    return when (unit) {
        "Kelvin" -> "$temperature °K"
        "Celsius" -> "$temperature ℃"
        "Fahrenheit" -> "$temperature ℉"
        else -> throw Exception("Undefined unit's type value")
    }
}