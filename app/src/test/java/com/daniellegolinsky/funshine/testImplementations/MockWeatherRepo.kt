package com.daniellegolinsky.funshine.testImplementations

import com.daniellegolinsky.funshine.data.IWeatherRepo
import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.ForecastTimestamp
import com.daniellegolinsky.funshine.models.ResponseOrError
import com.daniellegolinsky.funshine.models.api.CurrentWeatherResponse
import com.daniellegolinsky.funshine.models.api.DailyWeatherResponse
import com.daniellegolinsky.funshine.models.api.ForecastError
import com.daniellegolinsky.funshine.models.api.HourlyWeatherResponse
import com.daniellegolinsky.funshine.models.api.WeatherRequest

class MockWeatherRepo(private val failedRequest: Boolean = false): IWeatherRepo {

    private val mockForecast: Forecast = Forecast(
        timeCreated = ForecastTimestamp.getCurrentTimestamp(),
        currentWeather = CurrentWeatherResponse(
            temperature = 74.0f,
            weatherCodeInt = 0,
            isDay = 1,
            windSpeed = 3.0f
        ),
        dailyWeatherResponse = DailyWeatherResponse(
            weatherCodes = listOf(0, 0, 0, 0, 0, 0),
            maxTemp = listOf(80f, 79f, 78f, 77f, 76f, 75f),
            minTemp = listOf(70f, 69f, 68f, 67f, 66f, 65f),
            precipitationSum = listOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            precipitationProbabilityMax = listOf(0, 0, 0, 0, 0, 0),
        ),
        hourlyWeatherResponse = HourlyWeatherResponse(
            timeList = listOf("0", "1", "2", "3", "4", "5"),
            temperatureList = listOf(78f, 78f, 77f, 77f, 74f, 74f),
            humidityList = listOf(50, 50, 50, 50, 50, 50),
            precipitationProbability = listOf(0, 0, 0, 0, 0, 0)
        ),
    )

    override suspend fun getWeather(
        weatherRequest: WeatherRequest,
        forceUpdate: Boolean
    ): ResponseOrError<Forecast, ForecastError> {
        return if (!failedRequest) {
            ResponseOrError(isSuccess = true, data = mockForecast, error = null)
        } else {
            ResponseOrError(isSuccess = false, data = null, error = null) // TODO, make an error too?
        }
    }
}