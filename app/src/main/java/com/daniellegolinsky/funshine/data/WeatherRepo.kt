package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.api.OpenMeteoWeatherService
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.ForecastTimestamp
import com.daniellegolinsky.funshine.models.api.ForecastError
import com.daniellegolinsky.funshine.models.ResponseOrError
import com.daniellegolinsky.funshine.models.WeatherCode
import com.daniellegolinsky.funshine.models.api.WeatherRequest
import com.daniellegolinsky.funshine.models.api.WeatherResponse
import com.daniellegolinsky.funshine.models.toWeatherCode
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class WeatherRepo @Inject constructor(
    @Named(ApplicationModule.OPEN_METEO_WEATHER_SERVICE) private val weatherService: OpenMeteoWeatherService,
    private val settingsRepo: SettingsRepo,
    private val apiRequestLimiter: ApiRequestLimiter,
) {

    companion object{
        const val API_REQUEST_ERROR = "API_REQUEST_ERROR"
    }

    private val weatherMutex = Mutex()
    private var repoCachedWeather: Forecast? = null
    private var repoCachedWeatherResponse: ResponseOrError<Forecast, ForecastError>? = null
    private var repoCachedWeatherRequest: WeatherRequest? = null

    suspend fun getWeather(
        weatherRequest: WeatherRequest,
        forceUpdate: Boolean = false,
    ): ResponseOrError<Forecast, ForecastError> {
        // Reset the API limiter if necessary
        weatherMutex.withLock {
            var alwaysDoRequest = forceUpdate
            apiRequestLimiter.resetApiCallCounterAndTimestampIfValid()

            // If any parameters changed with the request, we MUST do a new request
            if (repoCachedWeatherRequest == null) {
                // Try to see if it's in the data store already
                repoCachedWeatherRequest = settingsRepo.getLastRequest()
            }
            // If it's not in the data store, we need to load it
            if (repoCachedWeatherRequest != null && weatherRequest != repoCachedWeatherRequest) {
                alwaysDoRequest = true
            }
            // Check if there is a stored version of the forecast before testing any cached weather
            if (!alwaysDoRequest && repoCachedWeather == null) {
                repoCachedWeather = settingsRepo.getLastForecast()
                if (repoCachedWeather != null) {
                    // A response was cached on disk, but not yet in memory
                    // Only cache successful forecast in repoCachedWeather
                    // NOTE: We could force a refresh here if the last request had a bad weather code,
                    //       however, that could spam the service in a DDOS if it's broken.
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
                }
            }

            // Only perform API actions if under the daily limit
            if (apiRequestLimiter.canMakeRequest()) {
                if (alwaysDoRequest
                    || repoCachedWeather == null
                    || repoCachedWeather?.timeCreated != getCurrentForecastTimestamp()
                ) {
                    // Do the request and cache the response locally
                    repoCachedWeatherResponse = makeApiRequest(
                        requestLatitude = weatherRequest.location.latitude,
                        requestLongitude = weatherRequest.location.longitude,
                        requestTempUnit = weatherRequest.tempUnit.toString(),
                        requestSpeedUnit = weatherRequest.speedUnit.toString(),
                        requestLengthUnit = weatherRequest.lengthUnit.toString(),
                    )
                    // Only cache successful forecast in repoCachedWeather
                    if (repoCachedWeatherResponse?.isSuccess == true) {
                        cacheSuccessfulForecast(
                            saveToSettings = true,
                            forecastToCache = repoCachedWeatherResponse!!.data!!,
                            requestForForecast = weatherRequest
                        )
                    }
                }
            } else { // Cannot make API request, too many within a 24 hour period
                // TODO Consider making this a popup and instead showing cached data
                val hoursLeft = apiRequestLimiter.hoursLeft()
                repoCachedWeatherResponse = ResponseOrError(
                    isSuccess = false,
                    data = null,
                    error = ForecastError(
                        isError = true,
                        errorMessage = API_REQUEST_ERROR,
                        hoursLeft = hoursLeft,
                    )
                )
            }
            return repoCachedWeatherResponse!!
        }
    }

    /**
     * Makes the API request and increments the API counter
     */
    private suspend fun makeApiRequest(
        requestLatitude: Float,
        requestLongitude: Float,
        requestTempUnit: String,
        requestSpeedUnit: String,
        requestLengthUnit: String,
    ): ResponseOrError<Forecast, ForecastError> {

        apiRequestLimiter.incrementApiCallCounter()

        var apiResponse: Response<WeatherResponse>? = try {
            weatherService.getCurrentWeather(
                latitude = requestLatitude,
                longitude = requestLongitude,
                tempUnit = requestTempUnit,
                speedUnit = requestSpeedUnit,
                lengthUnit = requestLengthUnit,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        return mapWeatherResponseToForecastOrError(
            apiResponse
        )
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
            this.settingsRepo.setLastRequest(this.repoCachedWeatherRequest!!)
        }
    }
}

private fun mapWeatherResponseToForecastOrError(
    response: Response<WeatherResponse>?
): ResponseOrError<Forecast, ForecastError> {
    return ResponseOrError(
        isSuccess = response?.isSuccessful ?: false,
        data = mapWeatherResponseToForecast(response?.body() ?: null),
        error = mapToForecastError(response?.errorBody() ?: null)
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

private fun getCurrentForecastTimestamp(): ForecastTimestamp {
    return ForecastTimestamp.getCurrentTimestamp()
}

private fun mapToForecastError(responseBody: ResponseBody?): ForecastError? {
    return responseBody?.let {
        ForecastError(
            isError = true,
            errorMessage = it.string() // TODO I do want to write a serializer...
        )
    } ?: null
}
