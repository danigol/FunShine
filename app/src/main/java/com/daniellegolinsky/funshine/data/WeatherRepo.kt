package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.api.OpenMeteoWeatherService
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.api.ForecastError
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.ResponseOrError
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.models.api.WeatherResponse
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Named

class WeatherRepo @Inject constructor(
    @Named(ApplicationModule.OPEN_METEO_WEATHER_SERVICE) val weatherService: OpenMeteoWeatherService
) {
    private val weatherMutex = Mutex()
    private var repoCachedWeather: Forecast? = null
    private var repoCachedWeatherResponse: ResponseOrError<Forecast, ForecastError>? = null

    suspend fun getWeather(
        location: Location,
        tempUnit: TemperatureUnit,
        speedUnit: SpeedUnit,
        lengthUnit: LengthUnit,
        forceUpdate: Boolean = false,
    ): ResponseOrError<Forecast, ForecastError> {
        if (forceUpdate || repoCachedWeather == null) {
            weatherMutex.withLock {
                repoCachedWeatherResponse = mapWeatherResponseToForecastOrError(
                    weatherService.getCurrentWeather(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        tempUnit = tempUnit.toString(),
                        speedUnit = speedUnit.toString(),
                        lengthUnit = lengthUnit.toString(),
                    )
                )
                // Only cache successful forecast in repoCachedWeather
                if (repoCachedWeatherResponse?.isSuccess == true) {
                    repoCachedWeather = repoCachedWeatherResponse!!.data
                }
            }
        }
        return weatherMutex.withLock { repoCachedWeatherResponse!! }
    }
}

private fun mapWeatherResponseToForecastOrError(
    response: Response<WeatherResponse>
): ResponseOrError<Forecast, ForecastError> {
    return ResponseOrError(
        isSuccess = response.isSuccessful,
        data = mapWeatherResponseToForecast(response?.body()),
        error = mapToForecastError(response?.errorBody())
    )
}

/**
 * val timeCreated: Long,
val currentWeather: CurrentWeatherResponse,
val dailyWeatherResponse: DailyWeatherResponse,
val hourlyWeatherResponse: HourlyWeatherResponse,
 */
private fun mapWeatherResponseToForecast(weatherResponse: WeatherResponse?): Forecast? {
    return weatherResponse?.let{
        Forecast(
            timeCreated = Calendar.getInstance().timeInMillis, // TODO See if we want to make this a different time
            currentWeather = it.currentWeather,
            dailyWeatherResponse = it.dailyWeatherResponse,
            hourlyWeatherResponse = it.hourlyWeatherResponse
        )
    } ?: null
}

private fun mapToForecastError(responseBody: ResponseBody?): ForecastError? {
    return responseBody?.let {
        ForecastError(
            isError = true,
            errorMessage = it.string() // TODO I do want to write a serializer...
        )
    } ?: null
}