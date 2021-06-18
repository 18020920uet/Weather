package com.example.weather.network.responses

data class GoogleSearchImageResponse(
    var items: List<BackgroundLink>
)

data class BackgroundLink(
    var link: String
)