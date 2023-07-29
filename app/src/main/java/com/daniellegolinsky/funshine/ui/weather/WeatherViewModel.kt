package com.daniellegolinsky.funshine.ui.weather

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniellegolinsky.funshine.R
import com.daniellegolinsky.funshine.data.SettingsRepo
import com.daniellegolinsky.themeresources.R.drawable
import com.daniellegolinsky.funshine.data.WeatherRepo
import com.daniellegolinsky.funshine.models.WeatherCode
import com.daniellegolinsky.funshine.models.api.CurrentWeatherResponse
import com.daniellegolinsky.funshine.models.getIconResource
import com.daniellegolinsky.funshine.models.getResourceStringForWeatherCode
import com.daniellegolinsky.funshine.viewstates.weather.WeatherScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val weatherRepo: WeatherRepo,
//    private val settingsRepo: SettingsRepo, TODO: F/C, in/cm, etc
) : ViewModel() {

    private val emptyState = WeatherScreenViewState(
        drawable.ic_loading_black,
        R.string.wc_unknown,
        null,
        context.getString(R.string.loading))
    private var _weatherViewState: MutableStateFlow<WeatherScreenViewState> =
        MutableStateFlow(emptyState)
    val weatherViewState: StateFlow<WeatherScreenViewState> = _weatherViewState

    fun getCurrentWeather() {
        // Show a loading screen
        if (_weatherViewState.value != emptyState) {
            _weatherViewState.value = emptyState
        }
        viewModelScope.launch {
            val currentWeatherResponse = weatherRepo.getCurrentWeather()
            val tempAsInt = currentWeatherResponse.temperature.toInt()
            val condition = currentWeatherResponse.weatherCode
            _weatherViewState.value = WeatherScreenViewState(
                condition.getIconResource(currentWeatherResponse.isDay == 1),
                condition.getResourceStringForWeatherCode(),
                tempAsInt,
                getForecastString(currentWeatherResponse)
            )
        }
    }

    private fun getForecastString(wr: CurrentWeatherResponse): String {
        return "${getWeatherCodeString(wr.weatherCode)} ${context.getString(R.string.currently)}." +
                "\n${context.getString(R.string.windspeed)} ${wr.windSpeed}${getWindspeedUnit()}."
    }

    private fun getWindspeedUnit(): String {
        // TODO Will replace with settings repo
        return context.getString(R.string.mph)
    }

    private fun getWeatherCodeString(wc: WeatherCode): String {
        return context.getString(wc.getResourceStringForWeatherCode())
    }
}