package com.example.weather.network

import com.example.weather.BuildConfig
import com.example.weather.network.responses.CurrentWeatherApiResponse
import com.example.weather.network.responses.RelatedNameLocation
import com.example.weather.network.responses.WeatherDetailResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.cdimascio.dotenv.Dotenv
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org"
private const val API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface OneCallApiService {
    @GET("data/2.5/onecall")
    fun getWeatherDeatilByCoordinatesAsync(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String,
        @Query("appid") APP_ID: String = API_KEY,
        @Query("exclude") exclude: String = "minutely,alerts"
    ): Deferred<WeatherDetailResponse>

    @GET("data/2.5/weather")
    fun getCurrentWeatherByCoordinatesAsync(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String,
        @Query("appid") APP_ID: String = API_KEY
    ): Deferred<CurrentWeatherApiResponse>

    @GET("data/2.5/weather")
    fun getCurrentWeatherByCityIDAsync(
        @Query("id") id: Int,
        @Query("lang") language: String,
        @Query("appid") APP_ID: String = API_KEY
    ): Deferred<CurrentWeatherApiResponse>

    @GET("geo/1.0/direct")
    fun getLocationByNameAsync(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 10,
        @Query("appid") APP_ID: String = API_KEY
    ): Deferred<List<RelatedNameLocation>>
}

object OpenWeatherApi {
    val retrofitService: OneCallApiService by lazy {
        retrofit.create(OneCallApiService::class.java)
    }
}