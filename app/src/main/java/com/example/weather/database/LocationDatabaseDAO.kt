package com.example.weather.database.entities

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LocationDatabaseDAO {
    @Insert
    suspend fun insert(location: Location)

    @Update
    suspend fun update(location: Location)

    @Delete
    suspend fun delete(location: Location)

    @Query("SELECT * FROM locations ORDER BY createdTime ASC")
    fun getLocations(): LiveData<List<Location>>

    @Query("SELECT * FROM locations WHERE isCurrentLocation = 1")
    suspend fun getCurrentLocation(): Location?
}