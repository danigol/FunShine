package com.daniellegolinsky.funshine.ui.weather

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniellegolinsky.funshine.R
import com.daniellegolinsky.funshine.data.SettingsRepo
import com.daniellegolinsky.themeresources.R.drawable
import com.daniellegolinsky.funshine.data.WeatherRepo
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.models.WeatherCode
import com.daniellegolinsky.funshine.models.api.WeatherResponse
import com.daniellegolinsky.funshine.models.getIconResource
import com.daniellegolinsky.funshine.models.getResourceStringForWeatherCode
import com.daniellegolinsky.funshine.viewstates.ViewState
import com.daniellegolinsky.funshine.viewstates.weather.WeatherScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val weatherRepo: WeatherRepo,
    private val settingsRepo: SettingsRepo,
) : ViewModel() {
    private val loadingState = WeatherScreenViewState(
        weatherIconResource = drawable.ic_loading_black,
        weatherIconContentDescription = R.string.wc_unknown,
        temperature = null,
        temperatureUnit = null,
        windspeedUnit = null,
        precipitationAmountUnit = null,
        forecast = context.getString(R.string.loading)
    )
    private var _weatherViewState: MutableStateFlow<ViewState<WeatherScreenViewState>> =
        MutableStateFlow(ViewState.Loading(loadingState))
    val weatherViewState: StateFlow<ViewState<WeatherScreenViewState>> = _weatherViewState

    fun loading() {
        // Prevent re-composition of any views using the state
        if (_weatherViewState.value !is ViewState.Loading) {
            _weatherViewState.value = ViewState.Loading(loadingState)
        }
    }

    fun loadForecast() {
        viewModelScope.launch {
            val weatherResponse = weatherRepo.getWeather(
                location = getLocation(),
                tempUnit = getTemperatureUnit(),
                speedUnit = getSpeedUnit(),
                lengthUnit = getLengthUnit(),
            )
            if (weatherResponse.isSuccessful) {
                weatherResponse.body()?.let { wr ->
                    val currentWeatherResponse = wr.currentWeather
                    val tempAsInt = currentWeatherResponse.temperature.toInt()
                    val tempUnitString = getTemperatureUnitInitial()
                    val speedUnitString = getSpeedUnit().toString()
                    val condition = currentWeatherResponse.weatherCode
                    val precipitationString = getLengthUnitString()

                    _weatherViewState.value = ViewState.Success(
                        WeatherScreenViewState(
                            weatherIconResource = condition.getIconResource(currentWeatherResponse.isDay == 1),
                            weatherIconContentDescription = condition.getResourceStringForWeatherCode(),
                            temperature = tempAsInt,
                            temperatureUnit = tempUnitString,
                            windspeedUnit = speedUnitString,
                            precipitationAmountUnit = precipitationString,
                            forecast = getForecastString(
                                wr = wr,
                                tempUnitString = tempUnitString,
                                windspeedUnitString = speedUnitString,
                                lengthUnitString = precipitationString,
                            )
                        )
                    )
                }
            } else { // Error returned
                _weatherViewState.value = ViewState.Error(
                    WeatherScreenViewState(
                        weatherIconResource = drawable.ic_circle_x_black,
                        weatherIconContentDescription = R.string.wc_unknown,
                        temperature = null,
                        temperatureUnit = null,
                        windspeedUnit = null,
                        precipitationAmountUnit = null,
                        forecast = "${
                            context.getString(
                                R.string.error_message,
                                "${weatherResponse.code()}"
                            )
                        }\n ${context.getString(R.string.error_help)}"
                    )
                )
            }
        }
    }

    private fun getForecastString(
        wr: WeatherResponse,
        tempUnitString: String,
        windspeedUnitString: String,
        lengthUnitString: String,
    ): String {
        val dailyWeatherResponse = wr.dailyWeatherResponse
        val currentWeatherResponse = wr.currentWeather
        val tempMaxAsInt = dailyWeatherResponse.maxTemp[0].toInt()
        val tempMinAsInt = dailyWeatherResponse.minTemp[0].toInt()
        val precipChance = dailyWeatherResponse.precipitationProbabilityMax[0]
        val precipAmount: Double = dailyWeatherResponse.precipitationSum[0]
        val hourlyWeatherResponse = wr.hourlyWeatherResponse

        val humidityString: String? = try {
            val humidityListSize = hourlyWeatherResponse.humidityList.size
            val humidity = hourlyWeatherResponse.humidityList[getCurrentHourIndex(humidityListSize)]
            "\n${context.getString(R.string.humidity)} ${humidity}%"
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        var weatherString =
            "${getWeatherCodeString(currentWeatherResponse.weatherCode)} ${context.getString(R.string.currently)}.\n" // Adds an extra space

        humidityString?.let {
            weatherString += it
        }
        weatherString += "\n${context.getString(R.string.windspeed)} ${currentWeatherResponse.windSpeed}${windspeedUnitString}" +
                "\n${context.getString(R.string.min_temp)} ${tempMinAsInt}${tempUnitString}" +
                "\n${context.getString(R.string.max_temp)} ${tempMaxAsInt}${tempUnitString}" +
                "\n${context.getString(R.string.precip_chance)} ${precipChance}%"
        if (precipChance > 0 && precipAmount > 0.010) {
            weatherString += "\n${context.getString(R.string.precip_max)} ${precipAmount}${lengthUnitString}"
        }
        return weatherString
    }

    private fun getWeatherCodeString(wc: WeatherCode): String {
        return context.getString(wc.getResourceStringForWeatherCode())
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
            context.getString(R.string.inch_abbreviation)
        }
    }
}