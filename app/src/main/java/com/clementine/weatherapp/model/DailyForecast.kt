package com.clementine.weatherapp.model

data class DailyForecast(
    val dayName: String,
    val date: String,
    val temp: String,
    val temp_ave: String,
    val description: String,
    val humidity: String,
    val pressure: Int
)
