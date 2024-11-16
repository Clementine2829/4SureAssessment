package com.clementine.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clementine.weatherapp.model.Forecast

class ForecastViewModel : ViewModel() {

    // List of forecasts (for the list fragment)
    private val _forecastList = MutableLiveData<List<Forecast>>()
    val forecastList: LiveData<List<Forecast>> = _forecastList

    // Currently selected forecast (for the detail fragment)
    private val _selectedForecast = MutableLiveData<Forecast?>()
    val selectedForecast: LiveData<Forecast?> = _selectedForecast

    init {
        // Initialize with sample data (you might load this from an API in a real app)
        _forecastList.value = listOf(
            Forecast("2023-04-01", "22.0", "Sunny"),
            Forecast("2023-04-02", "18.0", "Cloudy"),
            Forecast("2023-04-03", "20.0", "Rainy"),
            Forecast("2023-04-04", "25.0", "Thunderstorm"),
            Forecast("2023-04-05", "19.0", "Partly Cloudy")
        )
    }

    // Function to select a forecast item
    fun selectForecast(forecast: Forecast) {
        _selectedForecast.value = forecast
    }
}
