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
import com.clementine.weatherapp.databinding.FragmentForecastListBinding
import com.clementine.weatherapp.viewmodel.ForecastViewModel
import com.clementine.weatherapp.view.adapters.ForecastAdapter

class ForecastListFragment : Fragment() {

    private lateinit var binding: FragmentForecastListBinding
    private val viewModel: ForecastViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ForecastAdapter(viewModel.forecastList.value ?: emptyList()) { forecast ->
                viewModel.selectForecast(forecast)
            }
        }

        viewModel.forecastList.observe(viewLifecycleOwner) { forecastList ->
            (binding.recyclerView.adapter as ForecastAdapter).apply {
                (this as? ForecastAdapter)?.submitList(forecastList)
            }
        }
    }
}
