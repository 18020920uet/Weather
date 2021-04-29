package com.example.weatherapp

import android.app.Application
import timber.log.Timber

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}