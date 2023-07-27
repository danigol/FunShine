package com.daniellegolinsky.funshine.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniellegolinsky.funshine.data.WeatherRepo
import com.daniellegolinsky.funshine.viewstates.settings.SettingsViewState
import com.daniellegolinsky.funshine.viewstates.weather.WeatherScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepo: WeatherRepo) : ViewModel() {

    private val emptyState = WeatherScreenViewState(0, 72, "Fun in the FunShine")
    private var _weatherViewState: MutableStateFlow<WeatherScreenViewState> =
        MutableStateFlow(emptyState)
    val weatherViewState: StateFlow<WeatherScreenViewState> = _weatherViewState

    fun getCurrentWeather() {
        viewModelScope.launch {
            val tempAsInt = weatherRepo.getCurrentWeather().toInt()
            _weatherViewState.value = WeatherScreenViewState(0, tempAsInt, "It's ${tempAsInt}ºF and sunny.\nHeat index is like 105ºF\nGo cook an egg on your forehead, meatbag!")
        }
    }
}