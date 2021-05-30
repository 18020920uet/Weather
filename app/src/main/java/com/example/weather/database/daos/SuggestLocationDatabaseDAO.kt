package com.example.weather.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weather.database.entities.SuggestLocation

@Dao
interface SuggestLocationDatabaseDAO {
    @Query("SELECT * FROM suggest_locations")
    suspend fun getSuggestLocation(): List<SuggestLocation>

    @Insert
    fun insertMany(suggestLocations: List<SuggestLocation>)
}