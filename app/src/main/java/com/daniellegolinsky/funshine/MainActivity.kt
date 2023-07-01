package com.daniellegolinsky.funshine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.daniellegolinsky.designsystem.designelements.getBackgroundColor
import com.daniellegolinsky.funshine.databinding.ActivityMainBinding
import com.daniellegolinsky.funshine.ui.weather.WeatherScreen
import com.daniellegolinsky.funshine.viewstates.weather.WeatherScreenViewState

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(getBackgroundColor())) {// TODO, theme dependent
                WeatherScreen(
                    WeatherScreenViewState(
                        weatherCode = 0,
                        temperature = 74,
                        forecast = "Clear throughout the day\nHigh: 82ºF\nLow: 70ºF\nChance of rain: 0%"
                    ),
                )
            }
        }

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)

//        val navView: BottomNavigationView = binding.navView

//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        navView.setupWithNavController(navController)
    }
}