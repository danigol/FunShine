package com.daniellegolinsky.funshine.data

import android.util.Log
import com.daniellegolinsky.funshine.api.OpenMeteoWeatherService
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.ForecastTimestamp
import com.daniellegolinsky.funshine.models.api.ForecastError
import com.daniellegolinsky.funshine.models.ResponseOrError
import com.daniellegolinsky.funshine.models.api.WeatherRequest
import com.daniellegolinsky.funshine.models.api.WeatherResponse
import com.daniellegolinsky.funshine.models.hoursBetween
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Named

class WeatherRepo @Inject constructor(
    @Named(ApplicationModule.OPEN_METEO_WEATHER_SERVICE) private val weatherService: OpenMeteoWeatherService,
    private val settingsRepo: SettingsRepo,
    private val apiRequestLimiter: ApiRequestLimiter,
): IWeatherRepo {

    companion object{
        const val API_REQUEST_ERROR = "API_REQUEST_ERROR"
        const val LOG_INTERNET_CONNECTION_ERROR = "INTERNET_CONNECTION_ERROR"
        const val LOG_NULL_RESPONSE = "NULL_API_RESPONSE"
    }

    private val weatherMutex = Mutex()
    private var repoCachedWeather: Forecast? = null
    private var repoCachedWeatherResponse: ResponseOrError<Forecast, ForecastError>? = null
    private var repoCachedWeatherRequest: WeatherRequest? = null

    /**
     * Returns true if we need to do a new API request. Reasons to do a new API request:
     * - The requester is forcing it with `forceUpdate`
     * - There is no cached response in memory or in the dataStore
     * - The cached response in memory is older than an hour
     * - The cached response is a failure/error
     * - The new request is different than the one that generated the cached response
     */
    override suspend fun requiresApiRequest(
        weatherRequest: WeatherRequest,
        forceUpdate: Boolean,
    ): Boolean {
        weatherMutex.withLock {
            return if (forceUpdate) {
                true
            } else {
                // Ensure our local response is up to date
                updateLocalResponseCache()

                // If a cached response is over an hour old, we have to perform a new request
                // If there is no request, it'll default to true as well
                val hoursBetween = repoCachedWeatherResponse?.data?.timeCreated?.hoursBetween(ForecastTimestamp.getCurrentTimestamp()) ?: 24

                repoCachedWeatherResponse == null // No last request, need to make one
                        || repoCachedWeatherResponse?.isSuccess != true // Last request failed, need to retry
                        || hoursBetween >= 1 // Last request is old, need to refresh
                        || repoCachedWeatherRequest != null && repoCachedWeatherRequest != weatherRequest // Requests don't match, need to refresh
            }
        }
    }

    /**
     * Returns whatever response or error is currently cached locally
     */
    override suspend fun getCachedWeather(): ResponseOrError<Forecast, ForecastError>? {
        weatherMutex.withLock {
            // Ensure we have the latest possible response
            updateLocalResponseCache()
            // Return it (can be null)
            return repoCachedWeatherResponse
        }
    }

    override suspend fun getAndCacheWeather(
        weatherRequest: WeatherRequest,
        forceUpdate: Boolean, // TODO Eventally remove once logic is moved to requiresApiRequest
    ): ResponseOrError<Forecast, ForecastError> {
        // Reset the API limiter if necessary
        weatherMutex.withLock {
            apiRequestLimiter.resetApiCallCounterAndTimestampIfValid()
            // Only perform API actions if under the daily limit
            if (apiRequestLimiter.canMakeRequest()) {
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

        // Will increment only if we actually hit the API, set to increment by default
        var incrementApiLimiter = true

        var apiResponse: Response<WeatherResponse>? = try {
            weatherService.getCurrentWeather(
                latitude = requestLatitude,
                longitude = requestLongitude,
                tempUnit = requestTempUnit,
                speedUnit = requestSpeedUnit,
                lengthUnit = requestLengthUnit,
            )
        } catch (uhe: UnknownHostException) {
            // Catches the exception from no internet access
            uhe.printStackTrace()
            Log.e(LOG_INTERNET_CONNECTION_ERROR, "Exception in API Response: ${uhe.message}")
            // We know this didn't hit the API because it was an unknown host exception, so don't increment
            incrementApiLimiter = false
            null
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(LOG_NULL_RESPONSE, "Unknown exception: ${e.message}")
            null
        }

        if (apiResponse == null) {
            Log.e(LOG_NULL_RESPONSE, "API Response was null")
        }

        if (incrementApiLimiter) {
            apiRequestLimiter.incrementApiCallCounter()
            if (apiResponse == null) {
                Log.e(LOG_NULL_RESPONSE, "Incremented API, even though it's a null response.")
            }
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

    /**
     * If the in-memory cache is null, update it
     */
    private suspend fun updateLocalResponseCache() {
        if (this.repoCachedWeatherResponse == null && this.repoCachedWeather == null) {
            val dataStoreForecast = settingsRepo.getLastForecast()
            val dataStoreRequest = settingsRepo.getLastRequest()
            if (dataStoreForecast != null && dataStoreRequest != null) {
                // We only cache successful requests and responses, so we can assume it here
                cacheSuccessfulForecast(
                    saveToSettings = false,
                    forecastToCache = dataStoreForecast,
                    requestForForecast = dataStoreRequest,
                )
            }
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
