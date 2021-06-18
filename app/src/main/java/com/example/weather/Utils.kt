package com.example.weather

import com.example.weather.setting.PressureUnit
import com.example.weather.setting.SpeedUnit
import com.example.weather.setting.TemperatureUnit
import java.text.DecimalFormat

/**
 *  Convert temperature to setting's temperature unit
 *  @param  temperature - temperature value with default unit = K
 *  @param  unit - type of temperature unit: K - C - F
 *  @return result convert value
 */

fun convertTemperature(temperature: Double, unit: TemperatureUnit): Int {
    return when (unit) {
        TemperatureUnit.Kelvin -> temperature.toInt()
        TemperatureUnit.Celsius -> (temperature - 273.15).toInt()
        TemperatureUnit.Fahrenheit -> ((temperature - 273.15) * 9 / 5 + 32).toInt()
    }
}

fun getTemperatureText(temperature: Int, unit: TemperatureUnit): String {
    return when (unit) {
        TemperatureUnit.Kelvin -> "$temperature°K"
        TemperatureUnit.Celsius -> "$temperature℃"
        TemperatureUnit.Fahrenheit -> "$temperature℉"
    }
}

/**
 *  Convert temperature to setting's temperature unit
 *  @param  pressure - pressure value with default unit = hPa
 *  @param  unit - type of pressure unit: hPa - Pa
 *  @return result convert value
 */
fun convertPressure(pressure: Double, unit: PressureUnit): Int {
    return when (unit) {
        PressureUnit.hPa -> pressure.toInt()
        PressureUnit.Pa -> (pressure * 100).toInt()
    }
}

fun getPressureText(pressure: Int, unit: PressureUnit): String {
    return when (unit) {
        PressureUnit.hPa -> "$pressure hPa"
        PressureUnit.Pa -> "$pressure Pa"
    }
}

/**
 *  Convert temperature to setting's temperature unit
 *  @param  speed - speed value with default unit = metresPerSecond
 *  @param  unit - type of speed unit:
 *  @return result convert value
 */

fun convertSpeed(speed: Double, unit: SpeedUnit): Double {
    return when (unit) {
        SpeedUnit.metresPerSecond -> speed
        SpeedUnit.kilometersPerSHour -> speed * 3.6
        SpeedUnit.milesPerHour -> speed * 2.237
    }
}

fun getSpeedText(speed: Double, unit: SpeedUnit): String {
    val df = DecimalFormat("#.#")
    return when (unit) {
        SpeedUnit.metresPerSecond -> "${df.format(speed)} m/s"
        SpeedUnit.kilometersPerSHour -> "${df.format(speed)} km/h"
        SpeedUnit.milesPerHour -> "${df.format(speed)} mph"
    }
}