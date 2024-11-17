package com.clementine.weatherapp.model.remote

import com.clementine.weatherapp.model.CurrentWeatherResponse
import com.clementine.weatherapp.model.Forecast
import com.clementine.weatherapp.model.ReverseGeocodingResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    // Get the current weather data
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): CurrentWeatherResponse

    // Get the 5-day weather forecast
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): ResponseBody
}
