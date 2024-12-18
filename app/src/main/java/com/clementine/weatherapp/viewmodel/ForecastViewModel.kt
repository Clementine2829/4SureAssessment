package com.clementine.weatherapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clementine.weatherapp.model.CurrentWeatherResponse
import com.clementine.weatherapp.model.DailyForecast
import com.clementine.weatherapp.model.Forecast
import com.clementine.weatherapp.model.WeatherDatabaseHelper
import com.clementine.weatherapp.model.WeatherEntity
import com.clementine.weatherapp.network.RetrofitClient.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class ForecastViewModel : ViewModel() {

    private val _forecastList = MutableLiveData<List<Forecast>>()
    val forecastList: LiveData<List<Forecast>> = _forecastList

    private val _selectedForecast = MutableLiveData<Forecast?>()

    private val _currentWeather = MutableLiveData<CurrentWeatherResponse>()
    val currentWeather: LiveData<CurrentWeatherResponse> get() = _currentWeather

    private val _forecast = MutableLiveData<List<DailyForecast>>()
    val forecast: LiveData<List<DailyForecast>> get() = _forecast

    private val _weatherLogs = MutableLiveData<List<WeatherEntity>>()
    val weatherLogs: LiveData<List<WeatherEntity>> = _weatherLogs

    private val TAG = "ForecastViewModel"
    private val apiKey = "ab7186cefd34a6ff2e01237a2ea11e58"

    init {
        _forecastList.value = emptyList()
    }

    fun selectForecast(forecast: Forecast) {
        _selectedForecast.value = forecast
    }

    fun fetchCurrentWeather(latitude: Double, longitude: Double, unitType:String, context: Context) {
        viewModelScope.launch {
            try {
                val response = apiService.getCurrentWeather(latitude, longitude, apiKey)
                _currentWeather.value = response

                val dbHelper = WeatherDatabaseHelper(context)
                dbHelper.insertWeather(
                    townName = response.name,
                    latitude = latitude,
                    longitude = longitude,
                    weather = "${response.main.temp}",
                    unitType = unitType,
                    humidity = response.main.humidity,
                    description = response.weather[0].description,
                    pressure = response.main.pressure
                )


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val response: ResponseBody =
                    apiService.getWeatherForecast(latitude, longitude, apiKey)
                val jsonString = response.string()
                val jsonObject = JSONObject(jsonString)

                val forecastsArray: JSONArray = jsonObject.getJSONArray("list")
                val forecastList = mutableListOf<DailyForecast>()
                val uniqueDays = mutableSetOf<String>()

                for (i in 0 until forecastsArray.length()) {
                    val forecastObject: JSONObject = forecastsArray.getJSONObject(i)

                    val mainObject = forecastObject.getJSONObject("main")
                    val weatherArray: JSONArray = forecastObject.getJSONArray("weather")
                    val weatherObject: JSONObject = weatherArray.getJSONObject(0)

                    val dt = forecastObject.getLong("dt")
                    val dateTime = java.util.Date(dt * 1000) // Convert to milliseconds
                    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
                    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                    val date = dateFormat.format(dateTime)
                    val dayOfWeek = dayFormat.format(dateTime)

                    val temperatureMin = mainObject.getDouble("temp_min")
                    val temperatureMax = mainObject.getDouble("temp_max")
                    val pressure = mainObject.getInt("pressure")
                    val humidity = mainObject.getInt("humidity")
                    val description = weatherObject.getString("description")

                    if (!uniqueDays.contains(date)) {
                        uniqueDays.add(date)
                        forecastList.add(
                            DailyForecast(
                                dayOfWeek,
                                date,
                                temperatureMin,
                                temperatureMax,
                                description.replaceFirstChar { it.uppercase() },
                                "Humidity: ${humidity}%",
                                pressure,
                                ""
                            )
                        )
                    }
                }
                _forecast.value = forecastList
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching forecast", e)
            }
        }
    }
    fun loadWeatherLogs(context: Context) {
        val dbHelper = WeatherDatabaseHelper(context)
        val weatherList = dbHelper.getAllWeather()
        for (weather in weatherList) {
            println("Town: ${weather.townName}, Weather: ${weather.weather}, Description: ${weather.description}")
        }
        _weatherLogs.value = weatherList
    }

    fun getFormattedTemperature(): String {
        return "${currentWeather.value?.main?.temp?.toString() ?: "N/A"}°"
    }

    fun getFormattedPressure(): String {
        return "${currentWeather.value?.main?.pressure?.toString() ?: "N/A"}hPa"
    }
}
