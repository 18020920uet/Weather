package com.example.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.database.entities.Location
import com.example.weatherapp.database.entities.LocationDatabaseDAO

@Database(entities = [Location::class], version = 2, exportSchema = false)
abstract class WeatherAppDatabase : RoomDatabase() {

    abstract val locationDatabaseDAO: LocationDatabaseDAO

    companion object {
        @Volatile
        private var INSTANCE: WeatherAppDatabase? = null

        fun getInstance(context: Context): WeatherAppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherAppDatabase::class.java,
                        "weather_app_database"
                    )
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}