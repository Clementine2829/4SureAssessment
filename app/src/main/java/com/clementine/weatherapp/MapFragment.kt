package com.clementine.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class MapFragment : Fragment() {

    private lateinit var mapView: SupportMapFragment
    private lateinit var searchInput: TextInputEditText
    private lateinit var fabToggleTemp: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        searchInput = view.findViewById(R.id.search_location_edittext)
        fabToggleTemp = view.findViewById(R.id.fab_toggle_temp)

        mapView.getMapAsync { googleMap ->

        }

        fabToggleTemp.setOnClickListener {

        }

        return view
    }
}
