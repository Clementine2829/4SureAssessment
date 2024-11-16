package com.clementine.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ForecastListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var forecastAdapter: ForecastAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_forecast_list, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_forecast)
        forecastAdapter = ForecastAdapter(listOf())

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = forecastAdapter
        }

        return view
    }
}
