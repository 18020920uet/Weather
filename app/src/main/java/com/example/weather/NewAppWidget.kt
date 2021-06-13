package com.example.weather

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import androidx.preference.PreferenceManager
import com.example.weather.database.WeatherAppDatabase
import com.example.weather.database.entities.Location
import com.example.weather.network.OpenWeatherApi
import com.example.weather.setting.Settings
import com.example.weather.setting.TemperatureUnit
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    private var widgetJob = Job()
    private var widgetScope = CoroutineScope(Dispatchers.Main + widgetJob)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.new_app_widget)
        widgetScope.launch {
            val database = WeatherAppDatabase.getInstance(context).locationDatabaseDAO
            val location = database.getCurrentLocation()
            latitude = location!!.latitude
            longitude = location.longitude
            val settings = loadSettings(context)
            val widgetInfo = getCurrentLocationWeatherInformation(location)
            val temperatureText = getTemperatureText(
                convertTemperature(widgetInfo.temperature.toDouble(), settings.temperatureUnit),
                settings.temperatureUnit
            )
            val description = widgetInfo.description.capitalize()

            views.setImageViewResource(
                R.id.appwidget_icon,
                when (widgetInfo.icon) {
                    "mist_day" -> R.drawable.mist_day
                    "day" -> R.drawable.day
                    "cloudy_day" -> R.drawable.cloudy_day
                    "thunderstorm" -> R.drawable.thunderstorm
                    "rain" -> R.drawable.rain
                    "snow" -> R.drawable.snow

                    "sunset" -> R.drawable.sunset
                    "sunrise" -> R.drawable.sunrise

                    "mist_night" -> R.drawable.mist_night
                    "cloudy_night" -> R.drawable.cloudy_night
                    "night" -> R.drawable.night
                    else -> R.drawable.day
                }
            )
            views.setTextViewText(R.id.appwidget_city, widgetInfo.name.capitalize())
            views.setTextViewText(R.id.appwidget_temp, temperatureText)
            views.setTextViewText(R.id.appwidget_description, description)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private suspend fun getCurrentLocationWeatherInformation(location: Location): WidgetInfo {
        val setUp =
            OpenWeatherApi.retrofitService.getCurrentWeatherByCoordinatesAsync(
                location.latitude, location.longitude, "en"
            )
        val result = setUp.await()
        val icon: String
        if (result.datetime < result.sys.sunrise || result.datetime > result.sys.sunset) {
            icon = when (result.weather[0].id) {
                in 200..232 -> "thunderstorm"
                in 300..321 -> "mist_night"
                in 500..531 -> "rain"
                in 600..622 -> "snow"
                in 700..781 -> "mist_night"
                800 -> "night"
                else -> "cloudy_night"
            }
        } else {
            icon = when (result.weather[0].id) {
                in 200..232 -> "thunderstorm"
                in 300..321 -> "mist_day"
                in 500..531 -> "rain"
                in 600..622 -> "snow"
                in 700..781 -> "mist_day"
                800 -> "day"
                else -> "cloudy_day"
            }
        }
        return WidgetInfo(
            name = location.locationName,
            temperature = result.main.temp.toInt(),
            description = result.weather[0].description,
            icon = icon
        )
    }

    data class WidgetInfo(
        var name: String,
        var temperature: Int,
        var description: String,
        var icon: String
    )

    private fun loadSettings(context: Context): Settings {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)

        val temperatureUnit: TemperatureUnit =
            when (sp.getString("temperatureUnit", "Celsius")) {
                "Kelvin" -> TemperatureUnit.Kelvin
                "Fahrenheit" -> TemperatureUnit.Fahrenheit
                else -> TemperatureUnit.Celsius
            }
        return Settings(temperatureUnit)
    }
}

