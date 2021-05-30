package com.example.weather

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.example.weather.database.WeatherAppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

@RunWith(AndroidJUnit4::class)
class SuggestLocationsMigrationsTest {
    private lateinit var database: SupportSQLiteDatabase

    @JvmField
    @Rule
    val migrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        WeatherAppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrateInsertDataTest() {
        database = migrationTestHelper.createDatabase("test-migrate-database", 12 )
            .apply {
                execSQL(
                    "INSERT INTO suggest_locations VALUES (1566083,'Ho Chi Minh City',106.6667,10.75)".trimIndent()
                )
                close()
            }

        val weatherAppDatabase = Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            WeatherAppDatabase::class.java,
            "test-migrate-database"
        )
            .allowMainThreadQueries()
            .build()

        val suggestLocationsDAO = weatherAppDatabase.suggestLocationDatabaseDAO

        val suggestLocations = runBlocking {
            suggestLocationsDAO.getSuggestLocation()
        }

        Timber.i("$suggestLocations")

        assertEquals(suggestLocations.size, 1)
        assertEquals(suggestLocations[0].locationName, "Ho Chi Minh City")
    }
}