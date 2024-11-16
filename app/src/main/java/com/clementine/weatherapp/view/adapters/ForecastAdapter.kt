package com.clementine.weatherapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.clementine.weatherapp.databinding.ItemForecastBinding
import com.clementine.weatherapp.model.Forecast

class ForecastAdapter(
    private val forecasts: List<Forecast>,
    private val onClick: (Forecast) -> Unit
) : ListAdapter<Forecast, ForecastAdapter.ForecastViewHolder>(ForecastDiffCallback()) {

    inner class ForecastViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forecast: Forecast) {
            binding.forecast = forecast
            binding.root.setOnClickListener { onClick(forecast) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ItemForecastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(forecasts[position])
    }

    override fun getItemCount(): Int = forecasts.size
}
class ForecastDiffCallback : DiffUtil.ItemCallback<Forecast>() {
    override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
//        return oldItem.id == newItem.id
        return true
    }

    override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
        return oldItem == newItem
    }
}