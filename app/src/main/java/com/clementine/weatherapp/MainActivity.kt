package com.clementine.weatherapp

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.clementine.weatherapp.view.fragments.ForecastDetailFragment
import com.clementine.weatherapp.view.fragments.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Weather App"

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
        builder.setMessage("This is an example app to demonstrate a toolbar with an overflow menu.")
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}
