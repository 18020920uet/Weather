package com.example.weatherapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.preference.PreferenceManager
import com.example.weather.MainActivity
import com.example.weather.R
import com.example.weather.database.WeatherAppDatabase
import com.example.weather.database.daos.LocationDatabaseDAO
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

    lateinit var widgetInfo: WidgetInfo

    private var widgetJob = Job()
    private var widgetScope = CoroutineScope(Dispatchers.Main + widgetJob)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        val database = WeatherAppDatabase.getInstance(context).locationDatabaseDAO
        widgetScope.launch {
            val location = database.getCurrentLocation()
            latitude = location!!.latitude
            longitude = location.longitude
            val settings = loadSettings(context)
            widgetInfo = getCurrentLocationWeatherInformation(location)
            widgetInfo.temperatureUnit = settings.temperatureUnit
        }
    }

    private suspend fun getCurrentLocationWeatherInformation(location: Location): WidgetInfo {
        val setUp =
            OpenWeatherApi.retrofitService.getCurrentWeatherByCoordinatesAsync(
                location.latitude, location.longitude, "en"
            )
        val result = setUp.await()
        var icon: String
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
            temperatureUnit = TemperatureUnit.Celsius,
            icon = icon
        )
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }



    data class WidgetInfo(
        var name: String,
        var temperatureUnit: TemperatureUnit,
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

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widget: NewAppWidget.WidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId) as NewAppWidget.WidgetInfo

    val widgetLocationText = context.getString(R.string.my_location_text)
    val widgetTempText = context.getString(R.string.sample_temperature)
    val widgetText = context.getString(R.string.appwidget_text)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)

    views.setTextViewText(R.id.appwidget_city, widget.widgetInfo.name)
    views.setTextViewText(R.id.appwidget_temp, widget.widgetInfo.temperature.toString())
    views.setTextViewText(R.id.appwidget_description, widget.widgetInfo.description)
//    views.setImageViewResource(R.id.weather_icon, widget.icon.)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

