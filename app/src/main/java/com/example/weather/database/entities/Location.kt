package com.example.weather.database.entities


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Location(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var longitude: Double,
    var latitude: Double,
    var timeZoneOffSet: Long = 0L,
    var locationName: String,
    var country: String,
    var temperature: Int = 0,
    var createdTime: Long = System.currentTimeMillis(),
    var isCurrentLocation: Int// 1 true | 0 false
)
