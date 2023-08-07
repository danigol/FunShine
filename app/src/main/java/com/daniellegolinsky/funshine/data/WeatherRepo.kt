package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.api.OpenMeteoWeatherService
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.models.api.WeatherResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class WeatherRepo @Inject constructor(
    @Named(ApplicationModule.OPEN_METEO_WEATHER_SERVICE) val weatherService: OpenMeteoWeatherService
) {

    // TODO Caching
    suspend fun getWeather(
        location: Location,
        tempUnit: TemperatureUnit,
        speedUnit: SpeedUnit,
        lengthUnit: LengthUnit,
    ): Response<WeatherResponse> {
        return weatherService.getCurrentWeather(
            latitude = location.latitude,
            longitude = location.longitude,
            tempUnit = tempUnit.toString(),
            speedUnit = speedUnit.toString(),
            lengthUnit = lengthUnit.toString(),
        )
    }
}