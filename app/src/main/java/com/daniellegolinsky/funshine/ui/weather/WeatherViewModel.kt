package com.daniellegolinsky.funshine.ui.weather

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniellegolinsky.funshine.data.WeatherRepo
import com.daniellegolinsky.funshine.models.WeatherCode
import com.daniellegolinsky.funshine.models.getResourceStringForWeatherCode
import com.daniellegolinsky.funshine.viewstates.weather.WeatherScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val weatherRepo: WeatherRepo
) : ViewModel() {

    private val emptyState = WeatherScreenViewState(0, 72, "Fun in the FunShine")
    private var _weatherViewState: MutableStateFlow<WeatherScreenViewState> =
        MutableStateFlow(emptyState)
    val weatherViewState: StateFlow<WeatherScreenViewState> = _weatherViewState

    fun getCurrentWeather() {
        viewModelScope.launch {
            val currentWeatherResponse = weatherRepo.getCurrentWeather()
            val tempAsInt = currentWeatherResponse.temperature.toInt()
            val condition = currentWeatherResponse.weatherCode
            val itsDay = if (currentWeatherResponse.isDay == 1) { "during the day" } else { "at night" } // TODO Resources
            _weatherViewState.value = WeatherScreenViewState(
                0, // TODO pass on enum so we can extend it
                tempAsInt,
                "It's ${tempAsInt}ºF and ${getWeatherCodeString(condition)} ${itsDay}."
            )
        }
    }

    private fun getWeatherCodeString(wc: WeatherCode): String {
        return context.getString(wc.getResourceStringForWeatherCode())
    }
}