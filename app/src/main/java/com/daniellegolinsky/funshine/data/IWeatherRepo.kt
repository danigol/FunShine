package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.ResponseOrError
import com.daniellegolinsky.funshine.models.api.ForecastError
import com.daniellegolinsky.funshine.models.api.WeatherRequest

interface IWeatherRepo {

    suspend fun requiresApiRequest(
        weatherRequest: WeatherRequest,
        forceUpdate: Boolean = false,
    ): Boolean

    suspend fun getCachedWeather(): ResponseOrError<Forecast, ForecastError>?

    suspend fun getAndCacheWeather(
        weatherRequest: WeatherRequest,
        forceUpdate: Boolean = false,
    ): ResponseOrError<Forecast, ForecastError>
}
