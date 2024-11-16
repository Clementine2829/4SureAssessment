package com.clementine.weatherapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clementine.weatherapp.R
import com.clementine.weatherapp.model.DailyForecast

class DailyForecastAdapter(private var forecastList: List<DailyForecast>) :
    RecyclerView.Adapter<DailyForecastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayName: TextView = view.findViewById(R.id.dayName)
        val date: TextView = view.findViewById(R.id.date)
        val mainWeather: TextView = view.findViewById(R.id.mainWeather)
        val pressure: TextView = view.findViewById(R.id.pressure)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily_forecast, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.dayName.text = forecast.dayName
        holder.date.text = forecast.date
        holder.mainWeather.text = forecast.main
        holder.pressure.text = "${forecast.pressure} hPa"
    }

    override fun getItemCount() = forecastList.size

    fun updateForecasts(newForecasts: List<DailyForecast>) {
        forecastList = newForecasts
        notifyDataSetChanged()
    }
}
