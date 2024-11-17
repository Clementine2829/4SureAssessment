package com.clementine.weatherapp.model

data class WeatherEntity(
    val id: Int,
    val townName: String,
    val longitude: Double,
    val latitude: Double,
    val weather: String,
    val unitType: String,
    val humidity: Int,
    val description: String,
    val pressure: Int
)
