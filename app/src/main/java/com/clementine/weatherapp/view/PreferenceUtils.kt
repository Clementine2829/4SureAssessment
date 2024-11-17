package com.clementine.weatherapp.view

import android.content.Context
import android.content.SharedPreferences

object PreferenceUtils {
    private const val PREFS_NAME = "WeatherAppPrefs"
    private const val KEY_TEMP_UNIT = "temp_unit"

    fun getTemperatureUnit(context: Context): String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_TEMP_UNIT, "C") ?: "C"
    }
    fun celsiusToFahrenheit(celsius: Double): Double {
        val fahrenheit = celsius * 9 / 5 + 32
        return String.format("%.1f", fahrenheit).toDouble()
    }

}
