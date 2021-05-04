package com.example.weatherapp.database.entities


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Location(
    @PrimaryKey
    var id: Int,
    var longitude: Double,
    var latitude: Double,
    var timeZoneOffSet: Long = 0L,
    var city: String,
    var country: String,
    var temp: Double,
    var lastUpdated: Long = System.currentTimeMillis(),
    var isCurrentLocation: Int
)
