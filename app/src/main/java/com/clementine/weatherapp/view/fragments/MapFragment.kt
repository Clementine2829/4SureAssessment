package com.clementine.weatherapp.view.fragments

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.clementine.weatherapp.R
import com.clementine.weatherapp.databinding.FragmentMapBinding
import com.clementine.weatherapp.viewmodel.ForecastViewModel
import com.clementine.weatherapp.viewmodel.SettingsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: FragmentMapBinding
    private val forecastViewModel: ForecastViewModel by activityViewModels()
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        binding = FragmentMapBinding.bind(view)

        binding.fabToggleTemp.setOnClickListener {
            val location = binding.searchLocationEdittext.text.toString()
            if (location.isNotEmpty()) {
                searchLocation(location)
            } else {
                binding.searchLocationEdittext.error = "Please enter a location"
            }
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMapClickListener { latLng ->
            map.clear()
            map.addMarker(MarkerOptions().position(latLng))
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))

            forecastViewModel.fetchForecast(latLng.latitude, latLng.longitude)
            forecastViewModel.fetchCurrentWeather(
                latLng.latitude,
                latLng.longitude,
                settingsViewModel.temperatureUnit.value ?: "",
                requireContext()
            )
        }
    }

    private fun searchLocation(location: String) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocationName(location, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val latLng = LatLng(address.latitude, address.longitude)

                    map.clear()
                    map.addMarker(MarkerOptions().position(latLng).title(address.getAddressLine(0)))
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))

                    forecastViewModel.fetchForecast(latLng.latitude, latLng.longitude)
                    forecastViewModel.fetchCurrentWeather(
                        latLng.latitude,
                        latLng.longitude,
                        settingsViewModel.temperatureUnit.value ?: "",
                        requireContext()
                    )
                } else {
                    binding.searchLocationEdittext.error = "Location not found"
                    Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

}
