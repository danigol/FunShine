package com.daniellegolinsky.funshine

import com.daniellegolinsky.funshine.testImplementations.MockResourceProvider
import com.daniellegolinsky.funshine.testImplementations.MockSettingsRepo
import com.daniellegolinsky.funshine.testImplementations.MockWeatherRepo
import com.daniellegolinsky.funshine.ui.weather.WeatherViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Test

class WeatherViewModelTest {

    private val testResourceProvider = MockResourceProvider()

    @Test
    fun testInitialState() = runTest {
        val testViewModel: WeatherViewModel = WeatherViewModel(
            resourceProvider = testResourceProvider,
            weatherRepo = MockWeatherRepo(),
            settingsRepo = MockSettingsRepo(),
        )
    }
}