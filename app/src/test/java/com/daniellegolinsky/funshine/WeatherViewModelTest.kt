package com.daniellegolinsky.funshine

import com.daniellegolinsky.funshine.testImplementations.MockResourceProvider
import com.daniellegolinsky.funshine.ui.weather.WeatherViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Test

class WeatherViewModelTest {

    val testResourceProvider = MockResourceProvider()

    @Test
    fun testInitialState() = runTest {
//        val testViewModel: WeatherViewModel = WeatherViewModel(
//            testResourceProvider,
//            // TODO: mock settings repo
//            // TODO: Mock weather Repo
//        )
    }
}