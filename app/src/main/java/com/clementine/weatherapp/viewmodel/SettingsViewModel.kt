package com.clementine.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    private val _temperatureUnit = MutableLiveData<String>()
    val temperatureUnit: LiveData<String> = _temperatureUnit

    fun setTemperatureUnit(unit: String) {
        _temperatureUnit.value = unit
    }
}
