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
    var temperature: Int = 0,
    var isCurrentLocation: Int,
    var createdTime: Long = System.currentTimeMillis()
)
