package com.example.weather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.database.entities.Location
import com.example.weather.database.entities.LocationDatabaseDAO

@Database(entities = [Location::class], version = 9, exportSchema = false)
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