package com.example.weather.network

import io.github.cdimascio.dotenv.Dotenv;
import android.speech.tts.TextToSpeech
import com.example.weather.BuildConfig
import com.example.weather.network.responses.GoogleSearchImageResponse
import com.example.weather.network.responses.WeatherDetailResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com"
private const val API_KEY = BuildConfig.GOOGLE_SEARCH_ENGINE_API_KEY
private const val SEARCH_ENGINE_KEY = BuildConfig.GOOGLE_SEARCH_ENGINE_KEY

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface GoogleSearchImageApiServices {
    @GET("customsearch/v1")
    fun GetBackgrounds(
        @Query("key") key: String = API_KEY,
        @Query("cx") searchEngine: String = SEARCH_ENGINE_KEY,
        @Query("searchType") searchType: String = "image",
        @Query("q") searchValue: String
    ): Deferred<GoogleSearchImageResponse>
}

object GoogleSearchImageApi {
    val retrofitService: GoogleSearchImageApiServices by lazy {
        retrofit.create(GoogleSearchImageApiServices::class.java)
    }
}