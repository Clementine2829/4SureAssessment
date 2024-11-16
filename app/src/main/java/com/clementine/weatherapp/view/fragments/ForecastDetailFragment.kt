package com.clementine.weatherapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.clementine.weatherapp.R
import com.clementine.weatherapp.databinding.FragmentForecastDetailBinding
import com.clementine.weatherapp.viewmodel.ForecastViewModel

class ForecastDetailFragment : Fragment() {

    private lateinit var binding: FragmentForecastDetailBinding
    private val viewModel: ForecastViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }
}
