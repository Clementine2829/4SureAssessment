package com.clementine.weatherapp.model.repositories

import com.clementine.weatherapp.model.remote.WeatherApiService
import com.clementine.weatherapp.model.CurrentWeatherResponse
import com.clementine.weatherapp.model.Forecast
import com.clementine.weatherapp.model.ReverseGeocodingResponse

class WeatherRepository(private val weatherApiService: WeatherApiService) {

    suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeatherResponse {
        return weatherApiService.getCurrentWeather(latitude, longitude, API_KEY)
    }

    suspend fun getWeatherForecast(latitude: Double, longitude: Double): List<Forecast> {
        return weatherApiService.getWeatherForecast(latitude, longitude, API_KEY)
    }

    suspend fun reverseGeocode(latitude: Double, longitude: Double): List<ReverseGeocodingResponse> {
        return weatherApiService.reverseGeocode(latitude, longitude, API_KEY)
    }

    suspend fun searchLocations(query: String): List<ReverseGeocodingResponse> {
        return weatherApiService.searchLocations(query, API_KEY)
    }

    companion object {
        private const val API_KEY = "YOUR_API_KEY"
    }
}
