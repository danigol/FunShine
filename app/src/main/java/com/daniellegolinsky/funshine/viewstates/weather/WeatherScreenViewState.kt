package com.daniellegolinsky.funshine.viewstates.weather

data class WeatherScreenViewState(
    val weatherCode: Int,
    val temperature: Int,
    val forecast: String,
)
