package com.example.weather.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suggest_locations")
data class SuggestLocation(
    @PrimaryKey
    val locationId: Int,
    val locationName: String,
    val country: String,
    var longitude: Double,
    var latitude: Double
)
