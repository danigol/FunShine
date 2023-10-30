package com.daniellegolinsky.funshine.viewstates.weather


data class WeatherScreenViewState(
    val weatherIconResource: Int,
    val weatherIconContentDescription: Int,
    val temperature: Int,
    val temperatureUnit: String,
    val windspeedUnit: String?,
    val precipitationAmountUnit: String?,
    val forecast: String,
    val buttonsOnRight: Boolean = true,
)
