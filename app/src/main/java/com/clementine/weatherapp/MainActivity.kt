package com.clementine.weatherapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clementine.weatherapp.view.adapters.LogsAdapter
import com.clementine.weatherapp.view.fragments.ForecastDetailFragment
import com.clementine.weatherapp.view.fragments.MapFragment
import com.clementine.weatherapp.viewmodel.ForecastViewModel
import com.clementine.weatherapp.viewmodel.SettingsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "WeatherAppPrefs"
    private val KEY_TEMP_UNIT = "temp_unit"
    private val forecastViewModel: ForecastViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Weather App"
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MapFragment())
            .commit()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_map -> {
                    loadFragment(MapFragment())
                    true
                }

                R.id.nav_forecast -> {
                    loadFragment(ForecastDetailFragment())
                    true
                }

                else -> false
            }
        }

        handleBackPress()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun handleBackPress() {
        var backPressedTime: Long = 0
        val exitToastDuration = 2000
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    if (backPressedTime + exitToastDuration > System.currentTimeMillis()) {
                        finishAffinity()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Press back again to exit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    backPressedTime = System.currentTimeMillis()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                showAboutDialog()
                true
            }

            R.id.pref -> {
                displayTemperatureUnitDialog()
                true
            }

            R.id.logs -> {
                displayLogsDialog()
                true
            }

            R.id.close_app -> {
                closeApp()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun closeApp() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Close App")
        builder.setMessage("Are you sure you want to close this app?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            run {
                finishAffinity()
                dialog.dismiss()
            }
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun showAboutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("About")
        val aboutMessage = """
        App Version: 1.0.0
        Developed by: Clementine Mamogale
        Description: This is a simple weather app that provides weather updates based on your location.
    """.trimIndent()
        builder.setMessage(aboutMessage)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun displayTemperatureUnitDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_temperature_selection, null)
        dialogBuilder.setView(view)

        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val radioCelsius = view.findViewById<RadioButton>(R.id.radioCelsius)
        val radioFahrenheit = view.findViewById<RadioButton>(R.id.radioFahrenheit)
        val buttonSave = view.findViewById<Button>(R.id.buttonSave)

        val savedUnit = sharedPreferences.getString(KEY_TEMP_UNIT, "C")
        if (savedUnit == "C") {
            radioCelsius.isChecked = true
        } else {
            radioFahrenheit.isChecked = true
        }

        val dialog = dialogBuilder.create()
        dialog.show()

        buttonSave.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            val selectedUnit = if (selectedId == R.id.radioFahrenheit) "F" else "C"

            settingsViewModel.setTemperatureUnit(selectedUnit)
            with(sharedPreferences.edit()) {
                putString(KEY_TEMP_UNIT, selectedUnit)
                apply()
            }
            dialog.dismiss()

        }
    }

    private fun displayLogsDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.logs_dialog, null)
        dialogBuilder.setView(view)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewLogs)

        val dialog = dialogBuilder.create()
        dialog.show()

        recyclerView.layoutManager = LinearLayoutManager(this)
        forecastViewModel.weatherLogs.observe(this) { weatherLogs ->
            val adapter = LogsAdapter(weatherLogs)
            recyclerView.adapter = adapter
        }
        forecastViewModel.loadWeatherLogs(this)

        val buttonClose = view.findViewById<Button>(R.id.buttonClose)

        buttonClose.setOnClickListener {
            dialog.dismiss()
        }
    }
}
