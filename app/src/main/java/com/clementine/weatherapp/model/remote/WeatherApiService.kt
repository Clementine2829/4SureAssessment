package com.clementine.weatherapp.model.remote

import com.clementine.weatherapp.model.CurrentWeatherResponse
import com.clementine.weatherapp.model.Forecast
import com.clementine.weatherapp.model.ReverseGeocodingResponse
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
    ): List<Forecast>

    // Reverse geocoding to get address from coordinates
    @GET("geo/1.0/reverse")
    suspend fun reverseGeocode(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): List<ReverseGeocodingResponse>

    // Search locations by name
    @GET("geo/1.0/direct")
    suspend fun searchLocations(
        @Query("q") query: String,
        @Query("appid") apiKey: String
    ): List<ReverseGeocodingResponse>
}
