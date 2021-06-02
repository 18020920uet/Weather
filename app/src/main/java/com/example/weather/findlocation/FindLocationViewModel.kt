package com.example.weather.findlocation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.database.daos.SuggestLocationDatabaseDAO
import com.example.weather.database.entities.SuggestLocation
import com.example.weather.network.OpenWeatherApi
import com.example.weather.network.responses.RelatedNameLocation
import kotlinx.coroutines.*
import timber.log.Timber

class FindLocationViewModel(
    private val database: SuggestLocationDatabaseDAO,
    private val application: Application
) : ViewModel() {

    private var _listRelatedNameLocations = MutableLiveData<List<RelatedNameLocation>>()
    val listRelatedNameLocations: LiveData<List<RelatedNameLocation>>
        get() = _listRelatedNameLocations

    private var _listOfSuggestLocations = MutableLiveData<List<SuggestLocation>>()
    val listOfSuggestLocations: LiveData<List<SuggestLocation>>
        get() = _listOfSuggestLocations

    private var _notification = MutableLiveData<String>()
    val notification: LiveData<String>
        get() = _notification

    private var _navigateTo = MutableLiveData<String>()

    fun navigateToDetailFragment() {
        _navigateTo.value = "DetailFragment"
    }

    fun onNavigateCompleted() {
        _navigateTo.value = ""
        clearListRelatedNameLocations()
    }

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        loadSuggestLocations()
    }

    private fun loadSuggestLocations() {
        viewModelScope.launch {
            val suggestLocation: List<SuggestLocation> = getSuggestLocation()
            _listOfSuggestLocations.value = suggestLocation
        }
    }

    private suspend fun getSuggestLocation(): List<SuggestLocation> {
        return withContext(Dispatchers.IO) {
            database.getSuggestLocation()
        }
    }

    fun findLocationByName(locationName: String) {
        viewModelScope.launch {
            val setUp = OpenWeatherApi.retrofitService.getLocationByNameAsync(locationName)
            try {
                val result = setUp.await()
                if (result.isEmpty()) {
                    _notification.value = "Not found"
                } else {
                    _listRelatedNameLocations.value = result
                }
            } catch (e: Exception) {
                e.message?.let {
                    Timber.i(it)
                    if (it == "HTTP 404 Not Found") {
                        _notification.value = "Not found"
                    } else {
                        _notification.value = "Error"
                    }
                }
            }
        }
    }

    private fun clearListRelatedNameLocations() {
        _listRelatedNameLocations.value = null
    }

    fun onShowNotificationComplete() {
        _notification.value = ""
    }

}