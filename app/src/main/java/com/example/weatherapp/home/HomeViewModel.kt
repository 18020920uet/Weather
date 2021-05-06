package com.example.weatherapp.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.database.entities.Location
import com.example.weatherapp.database.entities.LocationDatabaseDAO
import com.example.weatherapp.network.OneCallApi
import kotlinx.coroutines.*
import timber.log.Timber

class HomeViewModel(val database: LocationDatabaseDAO, application: Application) :
    AndroidViewModel(application) {

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location>
        get() = _currentLocation


    private var _errorNotification = MutableLiveData<String>()
    val errorNotification: LiveData<String>
        get() = _errorNotification

    fun onShowErrorNotificationComplete() {
        _errorNotification.value = ""
    }

    private var viewModelJob = Job()
    private var viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        Timber.i("onInit")
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            val location = database.getCurrentLocation()
            if (location != null) {
                Timber.i("${location}")
                _currentLocation.value = location
                return@launch
            }
            Timber.i("Require current location")
        }
    }

    fun getCurrentLocationWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val lat = latitude
            val lon = longitude
            val getData = OneCallApi.retrofitService.getCurrentWeatherByCoordinatesAsync(lat, lon)
            try {
                val result = getData.await()
                val location = Location(
                    id = result.id,
                    longitude = longitude,
                    latitude = latitude,
                    timeZoneOffSet = result.offSetTimezone,
                    city = result.city,
                    country = result.sys.country,
                    isCurrentLocation = 1,
                    temp = result.main.temp
                )
                _currentLocation.value = location
                insertLocation(location)
            } catch (t: Throwable) {
                _errorNotification.value = t.message
            }
        }
    }

    private suspend fun insertLocation(location: Location) {
        return withContext(Dispatchers.IO) {
            database.insert(location)
        }
    }
}