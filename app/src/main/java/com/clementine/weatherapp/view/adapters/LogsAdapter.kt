package com.clementine.weatherapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clementine.weatherapp.R
import com.clementine.weatherapp.model.WeatherEntity

class LogsAdapter(private var weatherList: List<WeatherEntity>) :
    RecyclerView.Adapter<LogsAdapter.LogViewHolder>() {

    inner class LogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val townName: TextView = view.findViewById(R.id.tvTownName)
        val longitude: TextView = view.findViewById(R.id.lon)
        val latitude: TextView = view.findViewById(R.id.lat)
        val description: TextView = view.findViewById(R.id.description)
        val humidity: TextView = view.findViewById(R.id.humidity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.log_item, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val weather = weatherList[position]
        holder.townName.text = weather.townName
        holder.longitude.text = weather.longitude.toString()
        holder.latitude.text = weather.latitude.toString()
        holder.description.text = "${weather.weather}°${weather.unitType} ${weather.description}"
        holder.humidity.text = "${weather.humidity}%"
    }

    override fun getItemCount(): Int = weatherList.size
    fun updateLogs(newLogs: List<WeatherEntity>) {
        weatherList = newLogs
        notifyDataSetChanged()
    }
}
