package com.clementine.weatherapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.clementine.weatherapp.R
import com.clementine.weatherapp.databinding.FragmentForecastDetailBinding
import com.clementine.weatherapp.model.DailyForecast
import com.clementine.weatherapp.view.PreferenceUtils
import com.clementine.weatherapp.view.PreferenceUtils.celsiusToFahrenheit
import com.clementine.weatherapp.view.adapters.DailyForecastAdapter
import com.clementine.weatherapp.viewmodel.ForecastViewModel
import com.clementine.weatherapp.viewmodel.SettingsViewModel
import kotlin.text.*

class ForecastDetailFragment : Fragment() {

    private lateinit var binding: FragmentForecastDetailBinding
    private val forecastViewModel: ForecastViewModel by activityViewModels()
    private val settingsViewModel: SettingsViewModel by activityViewModels()
    private lateinit var forecastAdapter: DailyForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_forecast_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = forecastViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var temperature = 0.0
        forecastAdapter = DailyForecastAdapter(emptyList())
        binding.recyclerViewForecast.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewForecast.adapter = forecastAdapter

        settingsViewModel.setTemperatureUnit(PreferenceUtils.getTemperatureUnit(requireContext()))

        forecastViewModel.currentWeather.observe(viewLifecycleOwner) { weather ->
            val temperatureUnit = settingsViewModel.temperatureUnit.value ?: ""
            val temperature = weather.main.temp
            val description = weather?.weather?.get(0)?.description ?: "No Description"
            binding.tvTemperature.text = displayTemp(temperature, temperatureUnit)
            binding.tvTownName.text = weather?.name ?: "Unknown Location"
            binding.tvHumidity.text = "Huminity: ${weather?.main?.humidity ?: 0}%"
            binding.tvDescription.text = description.replaceFirstChar { it.uppercase() }
            binding.tvPressure.text = "Pressure: ${weather?.main?.pressure ?: 0} hPa"

            val weatherCondition = weather?.weather?.get(0)?.main ?: "Unknown"
            val imageView: ImageView = binding.tvTemperatureIcon
            val drawableRes = when (weatherCondition) {
                "Clear" -> R.drawable.ic_sunny
                "Clouds" -> R.drawable.ic_cloud
                "Rain" -> R.drawable.ic_rainy
                "Thunderstorm" -> R.drawable.ic_thunderstorm
                else -> null
            }
            if (drawableRes != null) {
                imageView.setImageResource(drawableRes)
            } else{
                imageView.visibility = View.GONE
            }
        }
        forecastViewModel.forecast.observe(viewLifecycleOwner) {
            forecastViewModel.forecast.value?.let {
                forecastAdapter.updateForecasts(
                    updateForecast(
                        it,
                        settingsViewModel.temperatureUnit.value ?: ""
                    )
                )
            }
        }
        settingsViewModel.temperatureUnit.observe(viewLifecycleOwner) { unit ->
            updateTemperatureDisplay(unit)
            temperature = forecastViewModel.currentWeather.value?.main?.temp ?: 0.0
            binding.tvTemperature.text = displayTemp(temperature, unit)
            forecastViewModel.forecast.value?.let {
                updateForecast(
                    it,
                    unit
                )
            }?.let { forecastAdapter.updateForecasts(it) }
        }
    }

    private fun updateForecast(forecast: List<DailyForecast>, unit: String): List<DailyForecast> {
        for (updatedForecast in forecast) {
            updatedForecast.tempUnit = unit
            updatedForecast.tempMin = celsiusToFahrenheit(updatedForecast.tempMin)
            updatedForecast.tempMax = celsiusToFahrenheit(updatedForecast.tempMax)
        }
        return forecast
    }

    private fun displayTemp(temp: Double, temperatureUnit: String): String {
        val template = if (temperatureUnit == "F") {
            celsiusToFahrenheit(temp)
        } else {
            temp
        }
        return "${String.format("%.1f", template)}°${temperatureUnit}"
    }

    private fun updateTemperatureDisplay(unit: String): String {
        return unit
    }
}
