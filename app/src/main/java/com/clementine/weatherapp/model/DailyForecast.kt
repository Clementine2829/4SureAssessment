package com.clementine.weatherapp.model

data class DailyForecast(
    val dayName: String,
    val date: String,
    var tempMin: Double,
    var tempMax: Double,
    val description: String,
    val humidity: String,
    val pressure: Int,
    var tempUnit: String
)
