package com.daniellegolinsky.funshine.models

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("temperature")
    val temperature: Float,

    @SerializedName("weathercode")
    val weatherCode: WeatherCode,

    @SerializedName("is_day")
    val isDay: Int,
)
