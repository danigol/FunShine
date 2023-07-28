package com.daniellegolinsky.funshine.viewstates.weather

data class WeatherScreenViewState(
    val weatherIconResource: Int,
    val weatherIconContentDescription: Int,
    val temperature: Int,
    val forecast: String,
)
