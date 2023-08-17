package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.api.OpenMeteoWeatherService
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.ForecastTimestamp
import com.daniellegolinsky.funshine.models.api.ForecastError
import com.daniellegolinsky.funshine.models.ResponseOrError
import com.daniellegolinsky.funshine.models.api.WeatherRequest
import com.daniellegolinsky.funshine.models.api.WeatherResponse
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.ResponseBody
import retrofit2.Response
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Named

class WeatherRepo @Inject constructor(
    @Named(ApplicationModule.OPEN_METEO_WEATHER_SERVICE) private val weatherService: OpenMeteoWeatherService,
    private val settingsRepo: SettingsRepo, // TODO: The fun bit, now we can also get it from here!
) {
    private val weatherMutex = Mutex()
    private var repoCachedWeather: Forecast? = null
    private var repoCachedWeatherResponse: ResponseOrError<Forecast, ForecastError>? = null
    private var repoCachedWeatherRequest: WeatherRequest? = null

    suspend fun getWeather(
        weatherRequest: WeatherRequest,
        forceUpdate: Boolean = false,
    ): ResponseOrError<Forecast, ForecastError> {
        // If any parameters changed with the request, we MUST do a new request
        var alwaysDoRequest = forceUpdate
        if (repoCachedWeatherRequest != null && weatherRequest != repoCachedWeatherRequest) {
            alwaysDoRequest = true
        }
        // Check if there is a stored version of the forecast before testing any cached weather
        if (!alwaysDoRequest && repoCachedWeather == null) {
            weatherMutex.withLock {
                repoCachedWeather = settingsRepo.getLastForecast()
                if (repoCachedWeather != null) {
                    // A response was cached on disk, but not yet in memory
                    // Only cache successful forecast in repoCachedWeather
                    cacheSuccessfulForecast(
                        saveToSettings = false,
                        forecastToCache = repoCachedWeather!!,
                        requestForForecast = weatherRequest
                    )
                    // Build out the cached return response
                    repoCachedWeatherResponse = ResponseOrError(
                        isSuccess = true,
                        data = repoCachedWeather,
                        error = null
                    )
//                    // We always cache successful requests too
//                    repoCachedWeatherRequest = weatherRequest
                }
            }
        }
        // Update cache and datastore if necessary or requested
        if (alwaysDoRequest
            || repoCachedWeather == null
            || repoCachedWeather?.timeCreated != getCurrentForecastTimestamp()
        ) {
            weatherMutex.withLock {
                // Do the request and cache the response locally
                repoCachedWeatherResponse = mapWeatherResponseToForecastOrError(
                    weatherService.getCurrentWeather(
                        latitude = weatherRequest.location.latitude,
                        longitude = weatherRequest.location.longitude,
                        tempUnit = weatherRequest.tempUnit.toString(),
                        speedUnit = weatherRequest.speedUnit.toString(),
                        lengthUnit = weatherRequest.lengthUnit.toString(),
                    )
                )
                // Only cache successful forecast in repoCachedWeather
                if (repoCachedWeatherResponse?.isSuccess == true) {
                    cacheSuccessfulForecast(
                        saveToSettings = true,
                        forecastToCache = repoCachedWeatherResponse!!.data!!,
                        requestForForecast = weatherRequest
                    )
//                    repoCachedWeather = repoCachedWeatherResponse!!.data
//                    settingsRepo.setLastForecast(repoCachedWeather!!)
//                    // We always cache successful requests too
//                    repoCachedWeatherRequest = weatherRequest
                }
            }
        }
        return weatherMutex.withLock { repoCachedWeatherResponse!! }
    }

    private suspend fun cacheSuccessfulForecast(
        saveToSettings: Boolean,
        forecastToCache: Forecast,
        requestForForecast: WeatherRequest,
    ){
        // Cache the successful forecast
        this.repoCachedWeather = forecastToCache

        // Cache the request that generated that data:
        this.repoCachedWeatherRequest = requestForForecast

        // Save to the settings repo, if requested
        // We wouldn't do this if loading from the data store, as it's the same data
        if (saveToSettings) {
            this.settingsRepo.setLastForecast(this.repoCachedWeather!!)
            // TODO Will also save request here
        }
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

private fun mapWeatherResponseToForecast(weatherResponse: WeatherResponse?): Forecast? {
    return weatherResponse?.let{
        Forecast(
            timeCreated = getCurrentForecastTimestamp(),
            currentWeather = it.currentWeather,
            dailyWeatherResponse = it.dailyWeatherResponse,
            hourlyWeatherResponse = it.hourlyWeatherResponse
        )
    } ?: null
}

// TODO Make this an extension function?
private fun getCurrentForecastTimestamp(): ForecastTimestamp {
    val currentHour = LocalDateTime.now().hour
    val currentDay = LocalDateTime.now().dayOfMonth
    val currentYear = LocalDateTime.now().year
    return ForecastTimestamp(year = currentYear, day = currentDay, hour = currentHour)
}

private fun mapToForecastError(responseBody: ResponseBody?): ForecastError? {
    return responseBody?.let {
        ForecastError(
            isError = true,
            errorMessage = it.string() // TODO I do want to write a serializer...
        )
    } ?: null
}