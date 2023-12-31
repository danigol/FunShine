package com.daniellegolinsky.funshine.models

import com.daniellegolinsky.funshine.models.api.CurrentWeatherResponse
import com.daniellegolinsky.funshine.models.api.DailyWeatherResponse
import com.daniellegolinsky.funshine.models.api.HourlyWeatherResponse
import kotlinx.serialization.Serializable

/**
 * Data class mapped from a response object. Used for caching, storage, and displaying
 * Does not contain HTML codes ore response information, just the data
 */
@Serializable
data class Forecast(
    val timeCreated: ForecastTimestamp,
    val currentWeather: CurrentWeatherResponse,
    val dailyWeatherResponse: DailyWeatherResponse,
    val hourlyWeatherResponse: HourlyWeatherResponse,
)
