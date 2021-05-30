package com.example.weather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weather.database.daos.LocationDatabaseDAO
import com.example.weather.database.daos.SuggestLocationDatabaseDAO
import com.example.weather.database.data.suggestLocationsData
import com.example.weather.database.entities.Location
import com.example.weather.database.entities.SuggestLocation
import java.util.concurrent.Executors

@Database(entities = [Location::class, SuggestLocation::class], version = 1, exportSchema = true)
abstract class WeatherAppDatabase : RoomDatabase() {

    abstract val locationDatabaseDAO: LocationDatabaseDAO
    abstract val suggestLocationDatabaseDAO: SuggestLocationDatabaseDAO

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
                        "weather_app.db"
                    ).addCallback(
                        object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                // insert the data on the IO Thread
                                Executors.newSingleThreadExecutor().execute {
                                    getInstance(context).suggestLocationDatabaseDAO
                                        .insertMany(suggestLocationsData)
                                }
                            }
                        }
                    )
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}