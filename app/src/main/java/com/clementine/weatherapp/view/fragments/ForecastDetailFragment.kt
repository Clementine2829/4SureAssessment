package com.clementine.weatherapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.clementine.weatherapp.R
import com.clementine.weatherapp.databinding.FragmentForecastDetailBinding
import com.clementine.weatherapp.view.adapters.DailyForecastAdapter
import com.clementine.weatherapp.viewmodel.ForecastViewModel

class ForecastDetailFragment : Fragment() {

    private lateinit var binding: FragmentForecastDetailBinding
    private val viewModel: ForecastViewModel by activityViewModels()
    private lateinit var forecastAdapter: DailyForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forecastAdapter = DailyForecastAdapter(emptyList())
        binding.recyclerViewForecast.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewForecast.adapter = forecastAdapter

        viewModel.currentWeather.observe(viewLifecycleOwner) { weather ->
            binding.tvTemperature.text = "${weather?.main?.temp?.let { String.format("%.1f", it) } ?: 0.0}°C"
            binding.tvTownName.text = weather?.name ?: "Unknown Location"
            binding.tvHumidity.text = "Huminity: ${weather?.main?.humidity ?: 0}%"
            binding.tvDescription.text = weather?.weather?.get(0)?.description ?: "No Description"
            binding.tvPressure.text = "Pressure: ${weather?.main?.pressure ?: 0} hPa"
        }

        viewModel.dailyForecasts.observe(viewLifecycleOwner) { forecasts ->
            forecastAdapter.updateForecasts(forecasts)
        }
    }
}
