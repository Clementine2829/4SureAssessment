package com.clementine.weatherapp.model

data class DailyForecast(
    val dayName: String,
    val date: String,
    val main: String,
    val pressure: Int
)
