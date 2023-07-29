package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.api.OpenMeteoWeatherService
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.api.CurrentWeatherResponse
import javax.inject.Inject
import javax.inject.Named

class WeatherRepo @Inject constructor(
    @Named(ApplicationModule.OPEN_METEO_WEATHER_SERVICE) val weatherService: OpenMeteoWeatherService
) {

    // TODO Caching
    suspend fun getCurrentWeather(): CurrentWeatherResponse {
        return weatherService.getCurrentWeather().currentWeather
    }
}