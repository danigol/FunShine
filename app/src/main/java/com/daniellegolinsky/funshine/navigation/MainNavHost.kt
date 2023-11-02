package com.daniellegolinsky.funshine.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import kotlinx.coroutines.flow.onCompletion

@Composable
fun MainNavHost(destination: String) {
    val navController = rememberNavController()
    val weatherViewModel = hiltViewModel<WeatherViewModel>()
    val settingsViewModel = hiltViewModel<SettingsViewModel>()

    NavHost(navController = navController, startDestination = destination) {
        composable(WEATHER) {
            WeatherScreen(
                viewModel = weatherViewModel,
                navigateToSettings = {
                    navController.navigate(SETTINGS)
                },
            )
            weatherViewModel.updateWeatherScreen()
        }
        composable(SETTINGS) {
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

