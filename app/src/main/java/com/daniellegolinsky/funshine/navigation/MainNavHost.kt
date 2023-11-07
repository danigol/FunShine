package com.daniellegolinsky.funshine.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daniellegolinsky.funshine.navigation.MainNavHost.ABOUT
import com.daniellegolinsky.funshine.navigation.MainNavHost.SETTINGS
import com.daniellegolinsky.funshine.navigation.MainNavHost.WEATHER
import com.daniellegolinsky.funshine.ui.about.AboutScreen
import com.daniellegolinsky.funshine.ui.settings.SettingsScreen
import com.daniellegolinsky.funshine.ui.settings.SettingsViewModel
import com.daniellegolinsky.funshine.ui.weather.WeatherScreen
import com.daniellegolinsky.funshine.ui.weather.WeatherViewModel

@Composable
fun MainNavHost(destination: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = destination) {

        composable(WEATHER) {
            val weatherViewModel = hiltViewModel<WeatherViewModel>()
            WeatherScreen(
                viewModel = weatherViewModel,
                navigateToSettings = {
                    navController.navigate(SETTINGS)
                },
            )
            weatherViewModel.updateWeatherScreen()
        }

        composable(SETTINGS) {
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(
                viewModel = settingsViewModel,
                returnToWeatherScreen = {
                    navController.navigate(WEATHER)
                },
                cancelAndGoBack  = {
                    navController.navigateUp()
                },
                navigateToAbout = {
                    navController.navigate(ABOUT)
                },
            )
        }

        composable(ABOUT) {
            AboutScreen(navigateUp = { navController.navigateUp() })
        }
    }
}

object MainNavHost {
    const val WEATHER = "weather"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
}

