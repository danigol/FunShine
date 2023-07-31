package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.api.OpenMeteoWeatherService
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.api.WeatherResponse
import javax.inject.Inject
import javax.inject.Named

class WeatherRepo @Inject constructor(
    @Named(ApplicationModule.OPEN_METEO_WEATHER_SERVICE) val weatherService: OpenMeteoWeatherService
) {

    // TODO Caching
    suspend fun getWeather(location: Location): WeatherResponse {
        return weatherService.getCurrentWeather(location.latitude, location.longitude)
    }
}