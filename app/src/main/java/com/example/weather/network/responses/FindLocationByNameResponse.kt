package com.example.weather.network.responses

import com.squareup.moshi.Json

data class RelatedNameLocation(
    val name: String,
    @Json(name = "lat")
    val latitude: Double,
    @Json(name = "lon")
    val longitude: Double,
    val country: String,
    val state: String?
)