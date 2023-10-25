package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.ResponseOrError
import com.daniellegolinsky.funshine.models.api.ForecastError
import com.daniellegolinsky.funshine.models.api.WeatherRequest

interface IWeatherRepo {
    suspend fun getWeather(
        weatherRequest: WeatherRequest,
        forceUpdate: Boolean
    ): ResponseOrError<Forecast, ForecastError>
}
