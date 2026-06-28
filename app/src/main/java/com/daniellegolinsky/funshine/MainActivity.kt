package com.daniellegolinsky.funshine

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.daniellegolinsky.funshinetheme.designelements.getBackgroundColor
import com.daniellegolinsky.funshine.navigation.MainNavHost
import com.daniellegolinsky.funshine.ui.settings.SettingsViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var locationCallback: LocationCallback

    private val settingsViewModel: SettingsViewModel by viewModels()

    private val isFoss = BuildConfig.BUILD_TYPE.lowercase().contains("foss")

    private var canGetLocation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Update location permission
        updatePermissionVariable()

        // This should only actually get called if we request location.
        // Though we may want to double-check location permissions here too
        setLocationCallback()

        setContent {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(getBackgroundColor())) {// TODO, theme dependent
                MainNavHost(destination = MainNavHost.WEATHER)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isFoss) {
            val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProvider.removeLocationUpdates(locationCallback)
        }
    }

    override fun onResume() {
        super.onResume()
        updatePermissionVariable()
        setLocationCallback()
    }

    private fun updatePermissionVariable() {
        canGetLocation = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setLocationCallback() {
        // This uses google libraries, we don't want those running in the foss versions
        if (!isFoss) {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    // Only run the function if we're already waiting on the location anyway
                    if (settingsViewModel.getHasRequestedLocation()) {
                        p0?.let { location -> // Curiously this is nullable in other builds, so to be safe...
                            settingsViewModel.respondToLocationChange(
                                locationGranted = canGetLocation,
                                locationResult = location
                            )
                        }
                    }
                }
            }
        }
    }
}
