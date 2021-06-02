package com.example.weather.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.database.daos.LocationDatabaseDAO
import com.example.weather.database.entities.Location
import com.example.weather.network.OpenWeatherApi
import com.example.weather.setting.Settings
import kotlinx.coroutines.*

class HomeViewModel(val database: LocationDatabaseDAO, application: Application) :
    AndroidViewModel(application) {

    private var _currentLocation = MutableLiveData<Location?>()
    val currentLocation: LiveData<Location?>
        get() = _currentLocation

    private var _notification = MutableLiveData<String>()
    val notification: LiveData<String>
        get() = _notification

    var watchingLocations: LiveData<List<Location>>

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
        watchingLocations = database.getWatchingLocations()
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
                location.latitude, location.longitude, language
            )
        try {
            val result = setUp.await()
            location.locationName = result.city
            location.temperature = result.main.temp.toInt()
            _currentLocation.value = location
        } catch (t: Throwable) {
            _notification.value = "Need reload"
        }
    }

    fun saveLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val language = settings.language ?: "en"
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
                    temperature = result.main.temp.toInt()
                )
                _currentLocation.value = location
                _notification.value = ""
                insertLocation(location)
            } catch (t: Throwable) {
                _notification.value = t.message
            }
        }
    }

//    fun loadLocationsWeatherInformation() {
//        viewModelScope.launch {
//            val locations = getLocations().await()
//            for (index in locations.indices) {
//                getLocationWeatherInformation(watchingLocations.value!![index])
//            }
//        }
//    }

//    private suspend fun getLocationWeatherInformation(location: Location) {
//        val language = settings.language ?: "en"
//        val setUp =
//            OpenWeatherApi.retrofitService.getCurrentWeatherByCoordinatesAsync(
//                location.latitude, location.longitude, language
//            )
//        try {
//            val result = setUp.await()
//            location.locationName = "HAHAHA"
//            location.temperature = result.main.temp.toInt()
//        } catch (t: Throwable) {
//            _notification.value = "Need reload"
//        }
//        watchingLocations.value = watchingLocations.value!!.plus(location)
//    }

    private suspend fun insertLocation(location: Location) {
        return withContext(Dispatchers.IO) {
            database.insert(location)
        }
    }

//    private suspend fun getLocations(): List<Location> {
//        return withContext(Dispatchers.IO) {
//            database.getLocations()
//        }
//    }
}
