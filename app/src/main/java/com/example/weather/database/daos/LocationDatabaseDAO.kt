package com.example.weather.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weather.database.entities.Location

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

    @Query("SELECT * FROM locations WHERE latitude= :latitude AND longitude=:longitude AND isCurrentLocation = 0")
    suspend fun getLocation(latitude: Double, longitude: Double): Location?
}