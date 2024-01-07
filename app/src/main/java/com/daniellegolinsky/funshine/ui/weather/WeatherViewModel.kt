package com.daniellegolinsky.funshine.ui.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniellegolinsky.funshine.R
import com.daniellegolinsky.funshine.data.ISettingsRepo
import com.daniellegolinsky.funshine.data.IWeatherRepo
import com.daniellegolinsky.funshine.data.WeatherRepo
import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.ForecastTimestamp
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.ResponseOrError
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.models.WeatherCode
import com.daniellegolinsky.funshine.models.api.ForecastError
import com.daniellegolinsky.funshine.models.api.WeatherRequest
import com.daniellegolinsky.funshine.models.getIconResource
import com.daniellegolinsky.funshine.models.getResourceStringForWeatherCode
import com.daniellegolinsky.funshine.models.toWeatherCode
import com.daniellegolinsky.funshine.utilities.IResourceProvider
import com.daniellegolinsky.funshine.viewstates.ViewState
import com.daniellegolinsky.funshine.viewstates.weather.WeatherScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val resourceProvider: IResourceProvider,
    private val weatherRepo: IWeatherRepo,
    private val settingsRepo: ISettingsRepo,
) : ViewModel() {
    private var _weatherViewState: MutableStateFlow<ViewState<WeatherScreenViewState>> =
        MutableStateFlow(ViewState.Loading())
    val weatherViewState: StateFlow<ViewState<WeatherScreenViewState>> = _weatherViewState

    private fun loading() {
        _weatherViewState.value = ViewState.Loading()
    }

    fun clearSettingsHint() {
        viewModelScope.launch {
            settingsRepo.setHasSeenSettingsHint(true)
        }
    }

    fun updateWeatherScreen() {
        // Make the request and update buttons
        viewModelScope.launch {
            val buttonsOnRight =
                settingsRepo.getWeatherButtonsOnRight()
            val shouldShowSettingsHint = !settingsRepo.getHasSeenSettingsHint() // TODO: Split out non-API UI items?
            val weatherRequest = WeatherRequest(
                location = getLocation(),
                tempUnit = getTemperatureUnit(),
                speedUnit = getSpeedUnit(),
                lengthUnit = getLengthUnit(),
            )
            val weatherResponse = if (weatherRepo.requiresApiRequest(weatherRequest)) {
                // Show the loading state
                loading()
                weatherRepo.getAndCacheWeather(weatherRequest)
            } else {
                // Either cached response or generic error response if it's null
                weatherRepo.getCachedWeather()
                    ?: ResponseOrError(
                        isSuccess = false,
                        data = null,
                        error = ForecastError(
                            isError = true,
                            errorMessage = resourceProvider.getString(R.string.unknown_error),
                        )
                    )
            }

            if (weatherResponse.isSuccess) {
                weatherResponse.data?.let { weatherResponseData ->
                    val currentWeatherResponse = weatherResponseData.currentWeather
                    val tempAsInt = currentWeatherResponse.temperature.toInt()
                    val tempUnitString = getTemperatureUnitInitial()
                    val speedUnitString = getSpeedUnit().toString()
                    val condition = currentWeatherResponse.weatherCodeInt.toWeatherCode()
                    val precipitationString = getLengthUnitString()

                    // TODO replace with viewstate builder function
                    _weatherViewState.value = ViewState.Success(
                        WeatherScreenViewState(
                            weatherIconResource = condition.getIconResource(currentWeatherResponse.isDay == 1),
                            weatherIconContentDescription = condition.getResourceStringForWeatherCode(),
                            temperature = tempAsInt,
                            temperatureUnit = tempUnitString,
                            windspeedUnit = speedUnitString,
                            precipitationAmountUnit = precipitationString,
                            forecast = getForecastString(
                                forecast = weatherResponseData,
                                tempUnitString = tempUnitString,
                                windspeedUnitString = speedUnitString,
                                lengthUnitString = precipitationString,
                            ),
                            buttonsOnRight = buttonsOnRight,
                            showSettingsHint = shouldShowSettingsHint,
                        )
                    )
                }
            } else { // Error returned
                val errorDetails = getErrorStringFromError(weatherResponse?.error)

                _weatherViewState.value = ViewState.Error(
                    errorString = "${
                        resourceProvider.getString(
                            R.string.error_message,
                            errorDetails
                        )
                    }\n ${resourceProvider.getString(R.string.error_help)}"
                )
            }
        }
    }

    private fun getErrorStringFromError(error: ForecastError?): String {
        return if (error != null) {
            if (error.errorMessage?.startsWith(WeatherRepo.API_REQUEST_ERROR) == true) {
                var hoursLeft = if (error.hoursLeft > 0) {
                    error?.hoursLeft
                } else {
                    ForecastTimestamp.HOURS_IN_DAY
                }

                // This should be impossible, but... TODO Remove after debugging?
                if (hoursLeft > 24) {
                    Log.e("HOURS_LEFT_BUG", "Hours left was: $hoursLeft")
                    hoursLeft = 25 // Will signal to the user (me) it reached this weird spot
                }

                resourceProvider.getString(
                    R.string.api_limit_error,
                    hoursLeft
                )
            } else {
                resourceProvider.getString(R.string.unknown_error)
            }
        } else {
            resourceProvider.getString(R.string.unknown_error)
        }
    }

    private fun getForecastString(
        forecast: Forecast,
        tempUnitString: String,
        windspeedUnitString: String,
        lengthUnitString: String,
    ): String {
        val dailyWeatherResponse = forecast.dailyWeatherResponse
        val currentWeatherResponse = forecast.currentWeather
        val hourlyWeatherResponse = forecast.hourlyWeatherResponse
        val tempMaxAsInt = dailyWeatherResponse.maxTemp[0].toInt()
        val tempMinAsInt = dailyWeatherResponse.minTemp[0].toInt()
        val hourlyPrecipitationChance =
            hourlyWeatherResponse.precipitationProbability[getCurrentHourIndex(hourlyWeatherResponse.precipitationProbability.size)]
        val dailyPrecipChance = dailyWeatherResponse.precipitationProbabilityMax[0]
        val dailyPrecipAmount: Double = dailyWeatherResponse.precipitationSum[0]

        val humidityString: String? = try {
            val humidityListSize = hourlyWeatherResponse.humidityList.size
            val humidity = hourlyWeatherResponse.humidityList[getCurrentHourIndex(humidityListSize)]
            "\n${resourceProvider.getString(R.string.humidity)} ${humidity}%"
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        var weatherString =
            "${getWeatherCodeString(currentWeatherResponse.weatherCodeInt.toWeatherCode())} ${
                resourceProvider.getString(
                    R.string.currently
                )
            }.\n" // Adds an extra space

        humidityString?.let {
            weatherString += it
        }
        weatherString += "\n${resourceProvider.getString(R.string.windspeed)} ${currentWeatherResponse.windSpeed}${windspeedUnitString}" +
                "\n${resourceProvider.getString(R.string.min_temp)} ${tempMinAsInt}${tempUnitString}" +
                "\n${resourceProvider.getString(R.string.max_temp)} ${tempMaxAsInt}${tempUnitString}" +
                "\n${resourceProvider.getString(R.string.precip_chance)} ${hourlyPrecipitationChance}%" +
                "\n${resourceProvider.getString(R.string.precip_chance_daily)} ${dailyPrecipChance}%"
        if (dailyPrecipChance > 0 && dailyPrecipAmount > 0.010) {
            weatherString += "\n${resourceProvider.getString(R.string.precip_max)} ${dailyPrecipAmount}${lengthUnitString}"
        }
        return weatherString
    }

    private fun getWeatherCodeString(wc: WeatherCode): String {
        return resourceProvider.getString(wc.getResourceStringForWeatherCode())
    }

    /**
     * Hourly forecasts store every item in a list of 24 elements.
     * [0] is always midnight
     * Therefore, the current time, as an int between 0-23, inclusive, is our time index
     * This just returns that index. It's made to not return if something is wrong with the input
     */
    private fun getCurrentHourIndex(listSize: Int): Int {
        val formatter = DateTimeFormatter.ofPattern("HH")
        val currentHour = LocalDateTime.now().format(formatter)
        var hour = 0
        try {
            hour = currentHour.toInt()
            if (hour < 0 || hour > listSize) {
                hour = 0
            }
        } catch (e: Exception) {
            Log.e("WeatherViewModel", e?.message ?: "")
        }
        return hour
    }

    private suspend fun getLocation(): Location {
        return settingsRepo.getLocation()
    }

    private suspend fun getTemperatureUnit(): TemperatureUnit {
        return settingsRepo.getTemperatureUnit()
    }

    private suspend fun getTemperatureUnitInitial(): String {
        return when (getTemperatureUnit()) {
            TemperatureUnit.CELSIUS -> "ºC" // TODO Resources!
            else -> "ºF"
        }
    }

    private suspend fun getSpeedUnit(): SpeedUnit {
        return settingsRepo.getSpeedUnit()
    }

    private suspend fun getLengthUnit(): LengthUnit {
        return settingsRepo.getLengthUnit()
    }

    /**
     * Millimeters are stored as "mm," which is fine for forecasts
     * However, inches is "inch," which should be abbreviated.
     */
    private suspend fun getLengthUnitString(): String {
        val lengthUnit = getLengthUnit()
        return if (lengthUnit == LengthUnit.MILLIMETER) {
            lengthUnit.toString()
        } else {
            resourceProvider.getString(R.string.inch_abbreviation)
        }
    }
}
