package com.clementine.weatherapp.model

data class ReverseGeocodingResponse(
    val lat: Double,
    val lon: Double,
    val name: String,
    val country: String
)