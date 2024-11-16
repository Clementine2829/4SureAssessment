package com.clementine.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clementine.weatherapp.model.CurrentWeatherResponse
import com.clementine.weatherapp.model.DailyForecast
import com.clementine.weatherapp.model.Forecast
import com.clementine.weatherapp.network.RetrofitClient.apiService
import kotlinx.coroutines.launch

class ForecastViewModel : ViewModel() {

    private val _forecastList = MutableLiveData<List<Forecast>>()
    val forecastList: LiveData<List<Forecast>> = _forecastList

    private val _selectedForecast = MutableLiveData<Forecast?>()
    val selectedForecast: LiveData<Forecast?> = _selectedForecast

    private val _currentWeather = MutableLiveData<CurrentWeatherResponse>()
    val currentWeather: LiveData<CurrentWeatherResponse> get() = _currentWeather

    private val _forecast = MutableLiveData<List<Forecast>>()
    val forecast: LiveData<List<Forecast>> get() = _forecast

    private val _dailyForecasts = MutableLiveData<List<DailyForecast>>()
    val dailyForecasts: LiveData<List<DailyForecast>> get() = _dailyForecasts

    private val TAG = "ForecastViewModel"
    private val apiKey = "ab7186cefd34a6ff2e01237a2ea11e58"

    init {
        _forecastList.value = emptyList()
    }

    fun selectForecast(forecast: Forecast) {
        _selectedForecast.value = forecast
    }

    fun fetchCurrentWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Fetching current weather for lat: $latitude, lon: $longitude")
                val response = apiService.getCurrentWeather(latitude, longitude, apiKey)
//                val fiveDaysList = apiService.getWeatherForecast(latitude, longitude, apiKey)
                _currentWeather.value = response
//                _dailyForecasts.value = fiveDaysList
                Log.d(TAG, "Current weather response: $response")
//                Log.d(TAG, "Current five Days List response: $fiveDaysList")
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching current weather", e)
            }
        }
    }

    fun fetchForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Fetching forecast for lat: $latitude, lon: $longitude")
                val forecastResponse = apiService.getWeatherForecast(latitude, longitude, apiKey)
                _forecast.value = forecastResponse
                Log.d(TAG, "Forecast response: $forecastResponse")
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching forecast", e)
            }
        }
    }
    fun fetchForecastList() {
        viewModelScope.launch {
            try {
                val items: List<DailyForecast> = listOf(
                    DailyForecast("Monday", "2024-11-17", "Sunny", 1015),
                    DailyForecast("Tuesday", "2024-11-18", "Cloudy", 1012),
                    DailyForecast("Wednesday", "2024-11-19", "Rainy", 1008),
                    DailyForecast("Thursday", "2024-11-20", "Windy", 1010),
                    DailyForecast("Friday", "2024-11-21", "Sunny", 1015)
                )
                _dailyForecasts.value = items
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching forecast", e)
            }
        }
    }

    fun fetchSearchedForecast(query: String) {
        if (query.isEmpty()) return
        viewModelScope.launch {
            try {
                val forecastResponse = apiService.searchLocations(query, apiKey)
//                _forecast.value = forecastResponse
                Log.d(TAG, "Forecast search response: $forecastResponse")
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching forecast", e)
            }
        }
    }

    fun getFormattedTemperature(): String {
        return "${currentWeather.value?.main?.temp?.toString() ?: "N/A"}°C"
    }

    fun getFormattedPressure(): String {
        return "${currentWeather.value?.main?.pressure?.toString() ?: "N/A"}hPa"
    }

}
