package com.daniellegolinsky.funshine

import com.daniellegolinsky.funshine.testImplementations.MockResourceProvider
import com.daniellegolinsky.funshine.testImplementations.MockSettingsRepo
import com.daniellegolinsky.funshine.testImplementations.MockWeatherRepo
import com.daniellegolinsky.funshine.ui.weather.WeatherViewModel
import com.daniellegolinsky.funshine.utilities.MainCoroutineRule
import com.daniellegolinsky.funshine.viewstates.ViewState
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class WeatherViewModelTest {

    private val testResourceProvider = MockResourceProvider()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun testInitialState() = runTest {
        val testViewModel = WeatherViewModel(
            resourceProvider = testResourceProvider,
            weatherRepo = MockWeatherRepo(),
            settingsRepo = MockSettingsRepo(),
        )
        assert(testViewModel.weatherViewState.value is ViewState.Loading)
    }

    @Test
    fun testSuccessfulCall() = runTest {
        val testViewModel = WeatherViewModel(
            resourceProvider = testResourceProvider,
            weatherRepo = MockWeatherRepo(),
            settingsRepo = MockSettingsRepo(),
        )
        testViewModel.loadForecast()
        advanceUntilIdle()
        assert(testViewModel.weatherViewState.value is ViewState.Success)
    }

    @Test
    fun testErrorCall() = runTest {
        val testViewModel = WeatherViewModel(
            resourceProvider = testResourceProvider,
            weatherRepo = MockWeatherRepo(failedRequest = true),
            settingsRepo = MockSettingsRepo(),
        )
        testViewModel.loadForecast()
        advanceUntilIdle()
        assert(testViewModel.weatherViewState.value is ViewState.Error)
    }
}
