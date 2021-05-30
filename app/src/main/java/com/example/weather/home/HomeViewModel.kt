package com.example.weather.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.convertTemperature
import com.example.weather.database.daos.LocationDatabaseDAO
import com.example.weather.database.entities.Location
import com.example.weather.network.OpenWeatherApi
import com.example.weather.setting.Settings
import kotlinx.coroutines.*
import timber.log.Timber

class HomeViewModel(val database: LocationDatabaseDAO, application: Application) :
    AndroidViewModel(application) {

    var lastUpdated: Long = 0L

    private var _currentLocation = MutableLiveData<Location?>()
    val currentLocation: LiveData<Location?>
        get() = _currentLocation

    private var _notification = MutableLiveData<String>()
    val notification: LiveData<String>
        get() = _notification

    fun onShowNotificationComplete() {
        _notification.value = ""
    }

    fun setNotification(notification: String) {
        _notification.value = notification
    }

    private var _navigateTo = MutableLiveData<String>()
    val navigateTo: LiveData<String>
        get() = _navigateTo

    fun navigateToDetailFragment() {
        _navigateTo.value = "DetailFragment"
    }

    fun onNavigateComplete() {
        _navigateTo.value = ""
    }

    var settings: Settings = Settings()

    private var viewModelJob = Job()
    private var viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            val location = database.getCurrentLocation()
            if (location == null) {
                _notification.value = "Require position"
            } else {
                getCurrentLocationWeatherInformation(location)
            }
        }
    }

    fun reload() = getCurrentLocation()


    private suspend fun getCurrentLocationWeatherInformation(location: Location) {
        val language = settings.language ?: "en"
        val setUp =
            OpenWeatherApi.retrofitService.getCurrentWeatherByCoordinatesAsync(
                location.latitude,
                location.longitude,
                language
            )
        try {
            val temperatureUnit = settings.temperatureUnit
            val result = setUp.await()
            location.locationName = result.city
            location.temperature =
                convertTemperature(result.main.temp, temperatureUnit)
            _currentLocation.value = location
            lastUpdated = System.currentTimeMillis()
        } catch (t: Throwable) {
            _notification.value = "Need reload"
        }
    }

    fun saveLocation(latitude: Double, longitude: Double) {
        Timber.i("saveLocationInfo")
        viewModelScope.launch {
            val language = settings.language ?: "en"
            val temperatureUnit = settings.temperatureUnit

            val setUp =
                OpenWeatherApi.retrofitService.getCurrentWeatherByCoordinatesAsync(
                    latitude, longitude, language
                )
            try {
                val result = setUp.await()
                val location = Location(
                    longitude = longitude,
                    latitude = latitude,
                    timeZoneOffSet = result.offSetTimezone,
                    locationName = result.city,
                    country = result.sys.country,
                    isCurrentLocation = 1,
                    temperature = convertTemperature(result.main.temp, temperatureUnit)
                )
                _currentLocation.value = location
                insertLocation(location)
            } catch (t: Throwable) {
                _notification.value = t.message
            }
        }
    }

    private suspend fun insertLocation(location: Location) {
        return withContext(Dispatchers.IO) { database.insert(location) }
    }
}